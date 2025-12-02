package use_case.akinator;

import data_access.PokeApiGateway;
import entity.PokemonTrait;
import entity.SimplePokemonProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AkinatorInteractorTest {

    private CapturingPresenter presenter;
    private AkinatorInteractor interactor;

    @BeforeEach
    void setUp() {
        presenter = new CapturingPresenter();
        interactor = new AkinatorInteractor(presenter, new StubGateway(), List.of());
    }

    @Test
    void startEmitsQuestion() {
        interactor.start();
        assertNotNull(presenter.last);
        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertTrue(presenter.last.isRoundActive());
    }

    @Test
    void startWhileActiveShowsError() {
        interactor.start();
        interactor.start();
        assertEquals("Finish or reset the current round first.", presenter.lastError);
    }

    @Test
    void constructorUsesProvidedProfiles() throws Exception {
        List<SimplePokemonProfile> provided = List.of(
                SimplePokemonProfile.of("custommon", PokemonTrait.AQUATIC));
        CapturingPresenter customPresenter = new CapturingPresenter();
        AkinatorInteractor customInteractor =
                new AkinatorInteractor(customPresenter, new StubGateway(), provided);

        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> knowledgeBase =
                (List<SimplePokemonProfile>) kbField.get(customInteractor);

        assertEquals(1, knowledgeBase.size());
        assertEquals("custommon", knowledgeBase.get(0).getName());
    }

    @Test
    void constructorWithNullProfilesSeedsDefaults() throws Exception {
        CapturingPresenter customPresenter = new CapturingPresenter();
        AkinatorInteractor customInteractor =
                new AkinatorInteractor(customPresenter, new StubGateway(), null);

        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> knowledgeBase =
                (List<SimplePokemonProfile>) kbField.get(customInteractor);

        assertFalse(knowledgeBase.isEmpty());
        assertEquals(AkinatorOutputData.Step.IDLE, customPresenter.last.getStep());
    }

    @Test
    void constructorWithEmptyProfilesFallsBackToDefaults() throws Exception {
        CapturingPresenter customPresenter = new CapturingPresenter();
        AkinatorInteractor customInteractor =
                new AkinatorInteractor(customPresenter, new StubGateway(), List.of());

        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> knowledgeBase =
                (List<SimplePokemonProfile>) kbField.get(customInteractor);

        assertFalse(knowledgeBase.isEmpty());
        assertEquals(AkinatorOutputData.Step.IDLE, customPresenter.last.getStep());
    }

    @Test
    void answeringBeforeStartShowsError() {
        interactor.answerYes();
        assertEquals("Press Start to get the first question.", presenter.lastError);
    }

    @Test
    void answeringUnknownMovesToNextClue() {
        interactor.start();

        interactor.answerUnknown();

        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertEquals("No worries, here’s another clue.", presenter.last.getStatus());
    }

    @Test
    void answeringWithMissingQuestionShowsErrorEvenWhenActive() throws Exception {
        setField("roundActive", true);
        setField("currentQuestion", null);

        interactor.answerNo();

        assertEquals("Press Start to get the first question.", presenter.lastError);
    }

    @Test
    void answeringWhileAwaitingGuessShowsError() throws Exception {
        setField("roundActive", true);
        setField("awaitingGuess", true);
        setField("currentQuestion", firstQuestion());

        interactor.answerYes();

        assertEquals("Answer the guess first.", presenter.lastError);
    }

    @Test
    void answeringWhileAwaitingRevealShowsError() throws Exception {
        setField("roundActive", true);
        setField("awaitingReveal", true);
        setField("currentQuestion", firstQuestion());

        interactor.answerNo();

        assertEquals("Tell me the Pokémon before we continue.", presenter.lastError);
    }

    @Test
    void eliminatingAllCandidatesRequestsReveal() throws Exception {
        setField("roundActive", true);
        setField("currentQuestion", firstQuestion());
        setField("candidates", new java.util.ArrayList<>());

        interactor.answerYes();

        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());
        assertEquals("Those clues don’t match anything I know. What was it?", presenter.last.getStatus());
    }

    @Test
    void answeringYesLeadsToGuessWhenOnlyOneCandidateRemains() throws Exception {
        useSmallKnowledgeBase();
        interactor.start();
        interactor.answerYes();

        assertNotNull(presenter.last);
        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void multipleLikelyCandidatesProduceShotAtItGuess() throws Exception {
        setField("roundActive", true);
        setField("currentQuestion", firstQuestion());
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("bulbasaur", PokemonTrait.STARTER),
                SimplePokemonProfile.of("squirtle", PokemonTrait.STARTER)
        )));

        interactor.answerYes();

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertEquals("Let me take a shot at it.", presenter.last.getStatus());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void pickNextQuestionProcessesEqualScoresBranch() throws Exception {
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("pokeA", PokemonTrait.STARTER),
                SimplePokemonProfile.of("pokeB", PokemonTrait.DUAL_TYPE)
        )));
        setField("askedTraits", java.util.EnumSet.noneOf(PokemonTrait.class));

        Method pick = AkinatorInteractor.class.getDeclaredMethod("pickNextQuestion");
        pick.setAccessible(true);
        Object question = pick.invoke(interactor);

        Method traitGetter = question.getClass().getDeclaredMethod("trait");
        traitGetter.setAccessible(true);
        assertEquals(PokemonTrait.STARTER, traitGetter.invoke(question));
    }

    @Test
    void hittingQuestionLimitForcesFinalGuessMessage() throws Exception {
        setField("roundActive", true);
        setField("currentQuestion", firstQuestion());
        setField("questionsAsked", 12);
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("p0", PokemonTrait.STARTER),
                SimplePokemonProfile.of("p1", PokemonTrait.DUAL_TYPE),
                SimplePokemonProfile.of("p2", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("p3", PokemonTrait.AQUATIC),
                SimplePokemonProfile.of("p4", PokemonTrait.DEFENSIVE_TANK)
        )));

        interactor.answerUnknown();

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertEquals("That’s my last question. Here’s my best guess!", presenter.last.getStatus());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void confirmGuessTrueFinishesRound() throws Exception {
        useSmallKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(true);

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertFalse(presenter.last.isRoundActive());
    }

    @Test
    void confirmGuessFalseTriggersAnotherGuess() throws Exception {
        useSmallKnowledgeBase();
        interactor.start();
        interactor.answerNo(); // keep multiple candidates
        interactor.confirmGuess(false);

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void revealRequestAcceptsPokemonName() throws Exception {
        useTinyKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(false);
        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());

        interactor.revealPokemon("Snorlax");

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertFalse(presenter.last.isRoundActive());
    }

    @Test
    void hittingQuestionLimitTriggersGuess() {
        interactor.start();
        for (int i = 0; i < 12 && (presenter.last == null
                || presenter.last.getStep() != AkinatorOutputData.Step.GUESS); i++) {
            interactor.answerUnknown();
        }

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void revealWithoutPromptShowsError() {
        interactor.revealPokemon("pikachu");
        assertEquals("I’m still narrowing it down.", presenter.lastError);
    }

    @Test
    void confirmingWithoutPendingGuessShowsError() {
        interactor.confirmGuess(true);
        assertEquals("No guess to confirm yet.", presenter.lastError);
    }

    @Test
    void confirmingWithEmptyCandidatesShowsError() throws Exception {
        setField("awaitingGuess", true);
        setField("roundActive", true);
        setField("candidates", new java.util.ArrayList<>());

        interactor.confirmGuess(true);

        assertEquals("No guess to confirm yet.", presenter.lastError);
    }

    @Test
    void confirmingWrongGuessWithNoRemainingCandidatesRequestsReveal() throws Exception {
        setField("awaitingGuess", true);
        setField("roundActive", true);
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("solo", PokemonTrait.AQUATIC)
        )));

        interactor.confirmGuess(false);

        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());
        assertEquals("Alright, help me out?", presenter.last.getStatus());
        assertTrue(presenter.last.isAwaitingReveal());
    }

    @Test
    void resetReturnsToIdleState() {
        interactor.start();
        interactor.answerUnknown();

        interactor.reset();

        assertNotNull(presenter.last);
        assertEquals(AkinatorOutputData.Step.IDLE, presenter.last.getStep());
        assertFalse(presenter.last.isRoundActive());
    }

    @Test
    void revealWithBlankNameSkipsApiLookup() throws Exception {
        useTinyKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(false);
        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());

        interactor.revealPokemon("   ");

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertNull(presenter.last.getGuessInfo());
    }

    @Test
    void revealWithNullNameSkipsApiLookup() throws Exception {
        useTinyKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(false);
        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());

        interactor.revealPokemon(null);

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertNull(presenter.last.getGuessInfo());
    }

    @Test
    void confirmCorrectWithBlankGuessNameSkipsApiLookup() throws Exception {
        setField("awaitingGuess", true);
        setField("roundActive", true);
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("   ", PokemonTrait.AQUATIC)
        )));

        interactor.confirmGuess(true);

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertNull(presenter.last.getGuessInfo());
    }

    @Test
    void startingWithNoUsefulQuestionsFallsBackToGuess() throws Exception {
        Field askedTraitsField = AkinatorInteractor.class.getDeclaredField("askedTraits");
        askedTraitsField.setAccessible(true);
        askedTraitsField.set(interactor, java.util.EnumSet.allOf(PokemonTrait.class));

        interactor.start();

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
        assertEquals("I’m out of good questions. Let me guess instead!", presenter.last.getStatus());
    }

    @Test
    void emptyCandidatesTriggerRevealRequest() throws Exception {
        Field candidatesField = AkinatorInteractor.class.getDeclaredField("candidates");
        candidatesField.setAccessible(true);
        candidatesField.set(interactor, new java.util.ArrayList<>());

        Method emitGuess = AkinatorInteractor.class.getDeclaredMethod("emitGuess", String.class);
        emitGuess.setAccessible(true);
        emitGuess.invoke(interactor, "test");

        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingReveal());
    }

    @Test
    void filterByTraitReturnsOriginalWhenFilterEmpty() throws Exception {
        Method filter = AkinatorInteractor.class.getDeclaredMethod("filterByTrait", List.class, PokemonTrait.class, boolean.class);
        filter.setAccessible(true);
        List<SimplePokemonProfile> base = List.of(SimplePokemonProfile.of("ditto", PokemonTrait.SPOOKY));

        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> result = (List<SimplePokemonProfile>) filter.invoke(interactor, base, PokemonTrait.STARTER, true);

        assertSame(base, result);
    }

    @Test
    void filterByTraitReturnsFilteredListWhenMatchesFound() throws Exception {
        Method filter = AkinatorInteractor.class.getDeclaredMethod("filterByTrait", List.class, PokemonTrait.class, boolean.class);
        filter.setAccessible(true);
        List<SimplePokemonProfile> base = List.of(
                SimplePokemonProfile.of("ditto", PokemonTrait.SPOOKY),
                SimplePokemonProfile.of("squirtle", PokemonTrait.AQUATIC)
        );

        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> result = (List<SimplePokemonProfile>) filter.invoke(interactor, base, PokemonTrait.AQUATIC, true);

        assertEquals(1, result.size());
        assertEquals("squirtle", result.get(0).getName());
        assertNotSame(base, result);
    }

    @Test
    void filterByTraitWithExpectedFalseFiltersOutTrait() throws Exception {
        Method filter = AkinatorInteractor.class.getDeclaredMethod("filterByTrait", List.class, PokemonTrait.class, boolean.class);
        filter.setAccessible(true);
        List<SimplePokemonProfile> base = List.of(
                SimplePokemonProfile.of("ditto", PokemonTrait.SPOOKY),
                SimplePokemonProfile.of("squirtle", PokemonTrait.AQUATIC)
        );

        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> result = (List<SimplePokemonProfile>) filter.invoke(interactor, base, PokemonTrait.SPOOKY, false);

        assertEquals(1, result.size());
        assertEquals("squirtle", result.get(0).getName());
    }

    @Test
    void singleCandidateFallsBackToFirstQuestion() throws Exception {
        useTinyKnowledgeBase();

        interactor.start();

        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertEquals("Is your Pokémon one of the starter Pokémon?", presenter.last.getPrompt());
    }

    @Test
    void uniformTraitCandidatesStillReturnAQuestion() throws Exception {
        useUniformKnowledgeBase(PokemonTrait.STARTER);

        interactor.start();

        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertEquals("Is your Pokémon one of the starter Pokémon?", presenter.last.getPrompt());
    }

    @Test
    void nonStarterCandidatesSkipImbalancedQuestionButStillAsk() throws Exception {
        useUniformKnowledgeBase(PokemonTrait.DEFENSIVE_TANK);

        interactor.start();

        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertEquals("Is your Pokémon one of the starter Pokémon?", presenter.last.getPrompt());
        assertEquals(1, presenter.last.getQuestionsAsked());
    }

    @Test
    void outputDataExposesCountsAndPromptDetails() throws Exception {
        useTinyKnowledgeBase();

        interactor.start();
        AkinatorOutputData out = presenter.last;

        assertEquals("Is your Pokémon one of the starter Pokémon?", out.getPrompt());
        assertEquals(1, out.getQuestionsAsked());
        assertEquals(12, out.getQuestionLimit());
        assertEquals(0, out.getRevealPromptId());
    }

    @Test
    void capitalizeHandlesNullAndBlank() throws Exception {
        Method capitalize = AkinatorInteractor.class.getDeclaredMethod("capitalize", String.class);
        capitalize.setAccessible(true);

        assertEquals("", capitalize.invoke(interactor, (Object) null));
        assertEquals("", capitalize.invoke(interactor, "   "));
        assertEquals("Bulbasaur", capitalize.invoke(interactor, "bULbASAUR"));
    }

    @Test
    void incorrectGuessWithLargeCandidatePoolRequestsNewQuestion() throws Exception {
        setField("awaitingGuess", true);
        setField("roundActive", true);
        setField("questionsAsked", 1);
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("p0", PokemonTrait.STARTER),
                SimplePokemonProfile.of("p1", PokemonTrait.DUAL_TYPE),
                SimplePokemonProfile.of("p2", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("p3", PokemonTrait.AQUATIC),
                SimplePokemonProfile.of("p4", PokemonTrait.DEFENSIVE_TANK)
        )));

        interactor.confirmGuess(false);

        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertEquals("Okay, another clue then.", presenter.last.getStatus());
        assertTrue(presenter.last.isRoundActive());
    }

    @Test
    void incorrectGuessAtQuestionLimitStillGuessesAgain() throws Exception {
        setField("awaitingGuess", true);
        setField("roundActive", true);
        setField("questionsAsked", 12);
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("p0", PokemonTrait.STARTER),
                SimplePokemonProfile.of("p1", PokemonTrait.DUAL_TYPE),
                SimplePokemonProfile.of("p2", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("p3", PokemonTrait.AQUATIC)
        )));

        interactor.confirmGuess(false);

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertEquals("Let me try another guess!", presenter.last.getStatus());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void incorrectGuessAtQuestionLimitWithLargePoolUsesQuestionLimitBranch() throws Exception {
        setField("awaitingGuess", true);
        setField("roundActive", true);
        setField("questionsAsked", 12);
        setField("candidates", new java.util.ArrayList<>(List.of(
                SimplePokemonProfile.of("p0", PokemonTrait.STARTER),
                SimplePokemonProfile.of("p1", PokemonTrait.DUAL_TYPE),
                SimplePokemonProfile.of("p2", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("p3", PokemonTrait.AQUATIC),
                SimplePokemonProfile.of("p4", PokemonTrait.DEFENSIVE_TANK)
        )));

        interactor.confirmGuess(false);

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertEquals("Let me try another guess!", presenter.last.getStatus());
    }

    @Test
    void answeringYesWithPlentyOfCandidatesAsksMoreQuestions() {
        interactor.start();
        interactor.answerYes();

        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertEquals("Got it, next clue:", presenter.last.getStatus());
    }

    @Test
    void pokeApiFailureIsReportedButGameContinues() throws Exception {
        presenter = new CapturingPresenter();
        interactor = new AkinatorInteractor(presenter, new FailingGateway(), List.of());
        useSmallKnowledgeBase();

        interactor.start();
        interactor.answerYes();

        assertEquals("Couldn’t reach PokéAPI, showing text only.", presenter.lastError);
        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
    }

    private void useSmallKnowledgeBase() throws Exception {
        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> base =
                (List<SimplePokemonProfile>) kbField.get(interactor);
        base.clear();
        base.addAll(Arrays.asList(
                SimplePokemonProfile.of("pikachu", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("eevee", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("charmander", PokemonTrait.STARTER)
        ));
        Method reset = AkinatorInteractor.class.getDeclaredMethod("resetRoundState");
        reset.setAccessible(true);
        reset.invoke(interactor);
    }

    private void useTinyKnowledgeBase() throws Exception {
        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> base =
                (List<SimplePokemonProfile>) kbField.get(interactor);
        base.clear();
        base.add(SimplePokemonProfile.of("snorlax", PokemonTrait.DEFENSIVE_TANK));
        Method reset = AkinatorInteractor.class.getDeclaredMethod("resetRoundState");
        reset.setAccessible(true);
        reset.invoke(interactor);
    }

    private void useUniformKnowledgeBase(PokemonTrait trait) throws Exception {
        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> base =
                (List<SimplePokemonProfile>) kbField.get(interactor);
        base.clear();
        base.addAll(List.of(
                SimplePokemonProfile.of("poke1", trait),
                SimplePokemonProfile.of("poke2", trait)
        ));
        Method reset = AkinatorInteractor.class.getDeclaredMethod("resetRoundState");
        reset.setAccessible(true);
        reset.invoke(interactor);
    }

    private Object firstQuestion() throws Exception {
        Field bank = AkinatorInteractor.class.getDeclaredField("questionBank");
        bank.setAccessible(true);
        List<?> questions = (List<?>) bank.get(interactor);
        return questions.get(0);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = AkinatorInteractor.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(interactor, value);
    }

    private static final class CapturingPresenter implements AkinatorOutputBoundary {
        private AkinatorOutputData last;
        private String lastError;

        @Override
        public void present(AkinatorOutputData outputData) {
            this.last = outputData;
        }

        @Override
        public void presentError(String message) {
            this.lastError = message;
        }
    }

    private static final class StubGateway extends PokeApiGateway {
        @Override
        public PokemonApiInfo fetchPokemon(String name) {
            return new PokemonApiInfo(
                    name,
                    null,
                    List.of("Test"),
                    1.0,
                    9.0
            );
        }
    }

    private static final class FailingGateway extends PokeApiGateway {
        @Override
        public PokemonApiInfo fetchPokemon(String name) throws IOException {
            throw new IOException("offline");
        }
    }
}
