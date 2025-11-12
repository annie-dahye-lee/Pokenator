package use_case.akinator;

import data_access.PokeApiGateway;
import entity.SimplePokemonProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AkinatorInteractor implements AkinatorInputBoundary{
    private final AkinatorOutputBoundary presenter;
    private final PokeApiGateway apiGateway;
    private final List<SimplePokemonProfile> knowledgeBase = Arrays.asList(
            new SimplePokemonProfile("pikachu", false, false, false),
            new SimplePokemonProfile("charizard", true, true, false),
            new SimplePokemonProfile("greninja", true, true, false),
            new SimplePokemonProfile("snorlax", false, false, false),
            new SimplePokemonProfile("mewtwo", false, false, true)
    );

    private final String[] questions = {
            "Is your Pokémon a starter Pokémon?",
            "Does your Pokémon have two types?",
            "Is your Pokémon legendary?"
    };

    private List<SimplePokemonProfile> candidates;
    private int questionIndex;
    private boolean awaitingGuess;

    public AkinatorInteractor(AkinatorOutputBoundary presenter,
                              PokeApiGateway apiGateway) {
        this.presenter = presenter;
        this.apiGateway = apiGateway;
        reset();
    }

    @Override
    public void start() {
        emitQuestion("Think of a Pokémon and answer the questions.");
    }

    @Override
    public void reset() {
        candidates = new ArrayList<>(knowledgeBase);
        questionIndex = 0;
        awaitingGuess = false;
        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.QUESTION,
                "Press Start to begin.",
                "",
                false,
                null));
    }

    @Override
    public void answerYes() {
        handleAnswer(true);
    }

    @Override
    public void answerNo() {
        handleAnswer(false);
    }

    @Override
    public void answerUnknown() {
        emitNextStep();
    }

    @Override
    public void confirmGuess(boolean correct) {
        if (!awaitingGuess || candidates.isEmpty()) {
            presenter.presentError("No guess to confirm yet.");
            return;
        }
        awaitingGuess = false;
        if (correct) {
            presenter.present(new AkinatorOutputData(
                    AkinatorOutputData.Step.FINISHED,
                    "Awesome! I guessed it.",
                    "",
                    false,
                    fetchInfo(candidates.get(0).getName())));
            reset();
        } else {
            candidates.remove(0);
            if (candidates.isEmpty()) {
                presenter.present(new AkinatorOutputData(
                        AkinatorOutputData.Step.FINISHED,
                        "You stumped me!",
                        "I’ll study more Pokémon.",
                        false,
                        null));
                reset();
            } else {
                emitNextStep();
            }
        }
    }

    private void handleAnswer(boolean answerYes) {
        if (awaitingGuess) {
            presenter.presentError("Answer the guess first.");
            return;
        }
        if (questionIndex >= questions.length) {
            emitGuess("I’m out of clues, here goes!");
            return;
        }

        String question = questions[questionIndex];
        questionIndex++;

        candidates = filter(candidates, questionIndex - 1, answerYes);
        if (candidates.isEmpty()) {
            presenter.present(new AkinatorOutputData(
                    AkinatorOutputData.Step.FINISHED,
                    "No Pokémon left match that.",
                    "Try another round!",
                    false,
                    null));
            reset();
            return;
        }

        emitNextStep();
    }

    private void emitNextStep() {
        if (candidates.size() == 1 || questionIndex >= questions.length) {
            emitGuess("I think I know it!");
        } else {
            emitQuestion("Got it, next clue:");
        }
    }

    private void emitQuestion(String status) {
        awaitingGuess = false;
        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.QUESTION,
                questions[questionIndex],
                status,
                false,
                null));
    }

    private void emitGuess(String status) {
        awaitingGuess = true;
        String name = candidates.get(0).getName();
        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.GUESS,
                "Is your Pokémon " + capitalize(name) + "?",
                status,
                true,
                fetchInfo(name)));
    }

    private List<SimplePokemonProfile> filter(List<SimplePokemonProfile> list,
                                              int questionIdx,
                                              boolean expected) {
        return switch (questionIdx) {
            case 1 -> keep(list, SimplePokemonProfile::isStarter, expected);
            case 2 -> keep(list, SimplePokemonProfile::isDualType, expected);
            case 3 -> keep(list, SimplePokemonProfile::isLegendary, expected);
            default -> list;
        };
    }

    private List<SimplePokemonProfile> keep(List<SimplePokemonProfile> source,
                                            BooleanPredicate predicate,
                                            boolean expected) {
        List<SimplePokemonProfile> filtered = new ArrayList<>();
        for (SimplePokemonProfile profile : source) {
            if (predicate.apply(profile) == expected) {
                filtered.add(profile);
            }
        }
        return filtered.isEmpty() ? source : filtered;
    }

    private PokeApiGateway.PokemonApiInfo fetchInfo(String name) {
        try {
            return apiGateway.fetchPokemon(name);
        } catch (IOException e) {
            presenter.presentError("Couldn’t reach PokéAPI, showing text only.");
            return null;
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isBlank()) return "";
        String lower = text.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    @FunctionalInterface
    private interface BooleanPredicate {
        boolean apply(SimplePokemonProfile profile);
    }
}
