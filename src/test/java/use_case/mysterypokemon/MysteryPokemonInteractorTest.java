package use_case.mysterypokemon;

import data_access.GameDataAccessInterface;
import data_access.PokemonDataAccessInterface;
import data_access.TypeFetcher;
import data_access.TypeMultiplierCalculator;
import entity.Game;
import entity.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MysteryPokemonInteractor without Mockito.
 * Uses in-memory fakes for DAOs and presenter.
 */
public class MysteryPokemonInteractorTest {

    private InMemoryPokemonDAO pokemonDAO;
    private InMemoryGameDAO gameDAO;
    private TypeMultiplierCalculator calculator;
    private TestPresenter presenter;
    private MysteryPokemonInteractor interactor;

    @BeforeEach
    void setUp() {
        pokemonDAO = new InMemoryPokemonDAO();

        gameDAO = new InMemoryGameDAO(new Game("Pikachu", 10, 0));

        calculator = new TypeMultiplierCalculator(new TypeFetcher());

        presenter = new TestPresenter();

        interactor = new MysteryPokemonInteractor(
                pokemonDAO,
                gameDAO,
                calculator,
                presenter
        );

        pokemonDAO.addPokemon(makePokemon("Pikachu",
                List.of("electric"), false, false, 320, "No Sprite Available"));
        pokemonDAO.addPokemon(makePokemon("Bulbasaur",
                List.of("grass", "poison"), false, false, 318, "No Sprite Available"));
    }

    private Pokemon makePokemon(String name,
                                List<String> types,
                                boolean legendary,
                                boolean mythical,
                                int totalBaseStats,
                                String spriteUrl) {
        return new Pokemon(
                name,
                new ArrayList<>(types),
                legendary,
                mythical,
                totalBaseStats,
                spriteUrl
        );
    }


    @Test
    void emptyGuessShowsErrorAndDoesNotChangeGame() throws IOException {
        MysteryPokemonInputData input = new MysteryPokemonInputData("");

        interactor.confirm(input);


        assertNull(presenter.lastSuccess);
        assertEquals("Please make a guess.", presenter.lastError);

        assertEquals(0, gameDAO.game.getCurrGuess());
        assertFalse(gameDAO.saved);
        assertFalse(gameDAO.deleted);
    }

    @Test
    void noActiveGameShowsStartFirstMessage() throws IOException {
        gameDAO.game = null;

        MysteryPokemonInputData input = new MysteryPokemonInputData("Pikachu");

        interactor.confirm(input);

        assertNull(presenter.lastSuccess);
        assertEquals("No active game. Please press Start first.", presenter.lastError);

        assertFalse(gameDAO.saved);
        assertFalse(gameDAO.deleted);
    }

    @Test
    void pokemonNotFoundShowsThatPokemonNotAvailable() throws IOException {
        MysteryPokemonInputData input = new MysteryPokemonInputData("Missingmon");

        interactor.confirm(input);

        assertNull(presenter.lastSuccess);
        assertEquals("That Pokémon is not available.", presenter.lastError);

        assertEquals(0, gameDAO.game.getCurrGuess());
        assertFalse(gameDAO.saved);
        assertFalse(gameDAO.deleted);
    }

    @Test
    void correctGuessEndsGameAndDeletesSave() throws IOException {
        MysteryPokemonInputData input = new MysteryPokemonInputData("Pikachu");

        interactor.confirm(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);


        assertEquals(0, gameDAO.lastSavedCurrGuess);
        assertEquals(0, gameDAO.initialCurrGuess);


        assertTrue(gameDAO.deleted);
        assertFalse(gameDAO.saved);
        assertNull(gameDAO.game);
    }

    @Test
    void wrongGuessConsumesGuessAndSavesGame() throws IOException {
        // Wrong guess = Bulbasaur
        MysteryPokemonInputData input = new MysteryPokemonInputData("Bulbasaur");

        interactor.confirm(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        // One guess should have been consumed
        assertEquals(1, gameDAO.game.getCurrGuess());
        assertEquals(9, gameDAO.game.getGuessesLeft());

        // Game should have been saved, not deleted
        assertTrue(gameDAO.saved);
        assertFalse(gameDAO.deleted);
    }

    @Test
    void wrongGuessAtLastLifeEndsGameAndDeletesSave() throws IOException {
        // New game with maxGuesses = 1, already 0 guesses used
        gameDAO = new InMemoryGameDAO(new Game("Pikachu", 1, 0));
        interactor = new MysteryPokemonInteractor(
                pokemonDAO,
                gameDAO,
                calculator,
                presenter
        );

        MysteryPokemonInputData input = new MysteryPokemonInputData("Bulbasaur");

        interactor.confirm(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        // Game should have been deleted after becoming over
        assertTrue(gameDAO.deleted);
        assertFalse(gameDAO.saved);
        assertNull(gameDAO.game);
    }

    @Test
    void quitDeletesGame() throws IOException {
        assertNotNull(gameDAO.game);

        interactor.quit();

        assertTrue(gameDAO.deleted);
        assertNull(gameDAO.game);
    }




    /**
     * In-memory Pokémon DAO that only supports getByName for tests.
     */
    private static class InMemoryPokemonDAO implements PokemonDataAccessInterface {
        private final Map<String, Pokemon> data = new HashMap<>();

        void addPokemon(Pokemon pokemon) {
            data.put(pokemon.getName().toLowerCase(), pokemon);
        }

        @Override
        public Pokemon getByName(String name) throws PokemonNotFoundException {
            Pokemon p = data.get(name.toLowerCase());
            if (p == null) {
                throw new PokemonNotFoundException("Not found: " + name);
            }
            return p;
        }

        // If your interface has more methods, add simple stubs here.
    }

    /**
     * In-memory Game DAO that tracks saves and deletes.
     */
    private static class InMemoryGameDAO implements GameDataAccessInterface {
        Game game;
        boolean saved = false;
        boolean deleted = false;

        int initialCurrGuess;
        int lastSavedCurrGuess;

        InMemoryGameDAO(Game initialGame) {
            this.game = initialGame;
            this.initialCurrGuess = initialGame.getCurrGuess();
        }

        @Override
        public Game loadGame() throws IOException {
            if (game == null) {
                throw new IllegalStateException("No saved game");
            }
            return game;
        }

        @Override
        public void saveGame(Game game) throws IOException {
            this.game = game;
            this.saved = true;
            this.lastSavedCurrGuess = game.getCurrGuess();
        }

        @Override
        public void deleteGame() throws IOException {
            this.game = null;
            this.deleted = true;
        }


        @Override
        public boolean hasSavedGame() { return game != null; }
    }

    /**
     * Simple presenter that just stores the last success or error.
     */
    private static class TestPresenter implements MysteryPokemonOutputBoundary {
        MysteryPokemonOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(MysteryPokemonOutputData outputData) {
            this.lastSuccess = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            this.lastSuccess = null;
        }
    }
}
