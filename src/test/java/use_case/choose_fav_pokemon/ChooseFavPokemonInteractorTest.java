package use_case.choose_fav_pokemon;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.themes.ThemeManager;
import org.junit.jupiter.api.Test;
import view.GameDashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ChooseFavPokemonInteractorTest {

    /**
     * Create a user and modify their favourite Pokémon through the Choose Favourite Pokémon use case
     */
    @Test
    void ChooseFavPokemonTest() {
        UserFactory userFactory = new UserFactory();
        FileUserDataAccessObject DAO = new FileUserDataAccessObject("users.csv", userFactory);

        User user = new User("Name", "PW", 10, "bio", "Charmander");
        DAO.save(user);
        ChooseFavPokemonInputData data = new ChooseFavPokemonInputData("Name", "PW", 10, "bio", "Charizard");

        ChooseFavPokemonOutputBoundary presenter = new ChooseFavPokemonOutputBoundary() {
            @Override
            public void prepareSuccessView(ChooseFavPokemonOutputData outputData) {
                //ensure favourite Pokémon has been modified
                assertEquals("Charizard", outputData.getFavPokemon());
                //ensure no other data is modified
                assertEquals("Name", user.getName());
                assertEquals("PW", user.getPassword());
                assertEquals("bio", user.getBio());
                assertEquals(10, user.getScore());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case fail is unexpected");
            }
        };

        ViewManagerModel viewManagerModel = new ViewManagerModel();
        ThemeManager themeManager = new ThemeManager();
        GameDashboard dashboard = new GameDashboard(viewManagerModel, themeManager);
        dashboard.setUser("Name");
        ChooseFavPokemonInputBoundary interactor = new ChooseFavPokemonInteractor(DAO, presenter, userFactory, dashboard);
        interactor.execute(data);
    }

    /**
     * Similar to previous test case, but user chooses the "None" option
     */
    @Test
    void ChooseNoPokemonTest() {
        UserFactory userFactory = new UserFactory();
        FileUserDataAccessObject DAO = new FileUserDataAccessObject("users.csv", userFactory);

        User user = new User("Name", "PW", 10, "bio", "Charmander");
        DAO.save(user);
        ChooseFavPokemonInputData data = new ChooseFavPokemonInputData("Name", "PW", 10, "bio", "None");

        ChooseFavPokemonOutputBoundary presenter = new ChooseFavPokemonOutputBoundary() {
            @Override
            public void prepareSuccessView(ChooseFavPokemonOutputData outputData) {
                //ensure favourite Pokémon has been modified
                assertEquals("None", outputData.getFavPokemon());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case fail is unexpected");
            }
        };

        ViewManagerModel viewManagerModel = new ViewManagerModel();
        ThemeManager themeManager = new ThemeManager();
        GameDashboard dashboard = new GameDashboard(viewManagerModel, themeManager);
        dashboard.setUser("Name");
        ChooseFavPokemonInputBoundary interactor = new ChooseFavPokemonInteractor(DAO, presenter, userFactory, dashboard);
        interactor.execute(data);
    }

    @Test
    void InvalidPokemonTest() {
        UserFactory userFactory = new UserFactory();
        FileUserDataAccessObject DAO = new FileUserDataAccessObject("users.csv", userFactory);

        User user = new User("Name", "PW", 10, "bio", "Charmander");
        DAO.save(user);
        ChooseFavPokemonInputData data = new ChooseFavPokemonInputData("Name", "PW", 10, "bio", "a");

        ChooseFavPokemonOutputBoundary presenter = new ChooseFavPokemonOutputBoundary() {
            @Override
            public void prepareSuccessView(ChooseFavPokemonOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Pokemon not found: a", errorMessage);
            }
        };

        ViewManagerModel viewManagerModel = new ViewManagerModel();
        ThemeManager themeManager = new ThemeManager();
        GameDashboard dashboard = new GameDashboard(viewManagerModel, themeManager);
        dashboard.setUser("Name");
        ChooseFavPokemonInputBoundary interactor = new ChooseFavPokemonInteractor(DAO, presenter, userFactory, dashboard);
        interactor.execute(data);
    }
}
