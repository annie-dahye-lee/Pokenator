package app;

import data_access.AkinatorKnowledgeBaseLoader;
import data_access.FileUserDataAccessObject;
import data_access.PokeApiGateway;
import entity.SimplePokemonProfile;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonController;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonPresenter;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonViewModel;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfilePresenter;
import interface_adapter.edit_profile.EditProfileViewModel;
import interface_adapter.user_profile.UserProfileController;
import interface_adapter.user_profile.UserProfilePresenter;
import interface_adapter.user_profile.UserProfileViewModel;
import interface_adapter.logged_in.*;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.settings.*;
import interface_adapter.settings.apply.ApplySettingsController;
import interface_adapter.settings.apply.ApplySettingsPresenter;
import interface_adapter.settings.back.BackSettingsController;
import interface_adapter.settings.back.BackSettingsPresenter;
import interface_adapter.settings.reset.ResetSettingsController;
import interface_adapter.settings.reset.ResetSettingsPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.themes.ThemeManager;
import interface_adapter.leaderboard.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.choose_fav_pokemon.ChooseFavPokemonInputBoundary;
import use_case.choose_fav_pokemon.ChooseFavPokemonInteractor;
import use_case.choose_fav_pokemon.ChooseFavPokemonOutputBoundary;
import use_case.edit_profile.*;
import use_case.user_profile.*;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.settings.apply.ApplySettingsInputBoundary;
import use_case.settings.apply.ApplySettingsInteractor;
import use_case.settings.apply.ApplySettingsOutputBoundary;
import use_case.settings.back.BackSettingsInputBoundary;
import use_case.settings.back.BackSettingsInteractor;
import use_case.settings.back.BackSettingsOutputBoundary;
import use_case.settings.reset.ResetSettingsInputBoundary;
import use_case.settings.reset.ResetSettingsInteractor;
import use_case.settings.reset.ResetSettingsOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.leaderboard.*;
import interface_adapter.akinator.AkinatorController;
import interface_adapter.akinator.AkinatorPresenter;
import interface_adapter.akinator.AkinatorViewModel;
import use_case.akinator.AkinatorInputBoundary;
import use_case.akinator.AkinatorInteractor;
import use_case.akinator.AkinatorOutputBoundary;
import view.*;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // DAO for user persistence
    private final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    // Views and view models
    private GameDashboard gameDashboard;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private SettingsViewModel settingsViewModel;
    private SettingsView settingsView;
    private EditProfileViewModel editProfileViewModel;
    private EditProfileView editProfileView;
    private ChooseFavPokemonViewModel chooseFavPokemonViewModel;
    private ChooseFavPokemonView chooseFavPokemonView;
    private AkinatorViewModel akinatorViewModel;
    private AkinatorView akinatorView;
    private LeaderboardViewModel leaderboardViewModel;
    private LeaderboardView leaderboardView;
    private UserProfileViewModel userProfileViewModel;
    private UserProfileView userProfileView;
    private final ThemeManager themeManager = new ThemeManager();

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }


    // ========== Add Views ==========

    public AppBuilder addGameDashboard() {
        gameDashboard = new GameDashboard(viewManagerModel, themeManager); // fixed: assign to field
        cardPanel.add(gameDashboard, gameDashboard.getViewName());

        // To change themes
        themeManager.registerView(gameDashboard);
        cardPanel.add(gameDashboard, gameDashboard.getViewName());

        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel, viewManagerModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addAkinatorView() {
        akinatorViewModel = new AkinatorViewModel();
        akinatorView = new AkinatorView(akinatorViewModel, viewManagerModel, themeManager);
        cardPanel.add(akinatorView, akinatorView.getViewName());
        themeManager.registerView(akinatorView);

        return this;
    }

    public AppBuilder addSettingsView() {
        settingsViewModel = new SettingsViewModel();
        settingsView = new SettingsView(settingsViewModel, themeManager);
        cardPanel.add(settingsView, settingsView.getViewName());
        themeManager.registerView(settingsView);
        return this;
    }

    public AppBuilder addLeaderboardView() {
        leaderboardViewModel = new LeaderboardViewModel();
        leaderboardView = new LeaderboardView(leaderboardViewModel, viewManagerModel, themeManager);
        cardPanel.add(leaderboardView, leaderboardView.getViewName());
        return this;
    }

    public AppBuilder addEditProfileView() {
        editProfileViewModel = new EditProfileViewModel(userDataAccessObject.get(gameDashboard.getCurrentUser()));
        editProfileView = new EditProfileView(editProfileViewModel, viewManagerModel, gameDashboard,
                                              userDataAccessObject, new PokeApiGateway());

        cardPanel.add(editProfileView, editProfileView.getViewName());
        return this;
    }

    public AppBuilder addChooseFavPokemonView() {
        chooseFavPokemonViewModel = new ChooseFavPokemonViewModel(userDataAccessObject.get(gameDashboard.getCurrentUser()));
        chooseFavPokemonView = new ChooseFavPokemonView(chooseFavPokemonViewModel, viewManagerModel, gameDashboard,
                userDataAccessObject, new PokeApiGateway());

        cardPanel.add(chooseFavPokemonView, chooseFavPokemonView.getViewName());
        return this;
    }

    public AppBuilder addUserProfileView() {
        // Initialize with a default user or null - will be updated when view is shown
        userProfileViewModel = new UserProfileViewModel();
        userProfileView = new UserProfileView(userProfileViewModel, viewManagerModel, gameDashboard,
                userDataAccessObject, new PokeApiGateway());

        cardPanel.add(userProfileView, userProfileView.getViewName());
        return this;
    }


    // ========== Add Use Cases ==========

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor =
                new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary =
                new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel, gameDashboard);

        final LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(viewManagerModel, loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addAkinatorUseCase() {
        AkinatorOutputBoundary presenter = new AkinatorPresenter(akinatorViewModel);
        List<SimplePokemonProfile> dynamicProfiles = Collections.emptyList();
        try {
            dynamicProfiles = new AkinatorKnowledgeBaseLoader().load(151);
            System.out.println("Pokénator: loaded " + dynamicProfiles.size() + " Pokémon from PokéAPI.");
        } catch (IOException e) {
            System.err.println("Pokénator: falling back to default roster (" + e.getMessage() + ")");
        }
        AkinatorInputBoundary interactor =
                new AkinatorInteractor(presenter, new PokeApiGateway(), dynamicProfiles);
        AkinatorController controller = new AkinatorController(interactor);
        akinatorView.setController(controller);
        return this;
    }

    public AppBuilder addResetSettingsUseCase() {

        ResetSettingsOutputBoundary presenter =
                new ResetSettingsPresenter(settingsViewModel, themeManager);

        ResetSettingsInputBoundary interactor =
                new ResetSettingsInteractor(presenter);

        ResetSettingsController controller =
                new ResetSettingsController(interactor);

        settingsView.setResetSettingsController(controller);
        return this;
    }

    public AppBuilder addAccessSettingsUseCase() {

        BackSettingsOutputBoundary presenter =
                new BackSettingsPresenter(viewManagerModel, settingsViewModel, "dashboard");

        BackSettingsInputBoundary interactor =
                new BackSettingsInteractor(presenter);

        BackSettingsController controller =
                new BackSettingsController(interactor);

        settingsView.setAccessSettingsController(controller);
        return this;
    }

    public AppBuilder addSaveSettingsUseCase() {

        ApplySettingsOutputBoundary presenter =
                new ApplySettingsPresenter(viewManagerModel, settingsViewModel, themeManager);

        ApplySettingsInputBoundary interactor =
                new ApplySettingsInteractor(presenter);

        ApplySettingsController controller =
                new ApplySettingsController(interactor);

        settingsView.setSaveSettingsController(controller);
        return this;
    }

    public AppBuilder addEditProfileUseCase() {

        EditProfileOutputBoundary presenter =
                new EditProfilePresenter(editProfileViewModel, viewManagerModel);

        EditProfileInputBoundary interactor =
                new EditProfileInteractor(userDataAccessObject, presenter, userFactory, gameDashboard);

        EditProfileController controller = new EditProfileController(interactor);

        editProfileView.setEditProfileController(controller);
        return this;
    }

    public AppBuilder addChooseFavPokemonUseCase() {

        ChooseFavPokemonOutputBoundary presenter =
                new ChooseFavPokemonPresenter(chooseFavPokemonViewModel, viewManagerModel);

        ChooseFavPokemonInputBoundary interactor =
                new ChooseFavPokemonInteractor(userDataAccessObject, presenter, userFactory, gameDashboard);

        ChooseFavPokemonController controller = new ChooseFavPokemonController(interactor);

        chooseFavPokemonView.setChooseFavPokemonController(controller);
        return this;
    }
    public AppBuilder addLeaderboardUseCase() {

        LeaderboardOutputBoundary presenter =
                new LeaderboardPresenter(leaderboardViewModel);

        LeaderboardInputBoundary interactor =
                new LeaderboardInteractor(userDataAccessObject, presenter);

        LeaderboardController controller =
                new LeaderboardController(interactor);

        leaderboardView.setLeaderboardController(controller);

        // Preemptively set up page 1.
        controller.changePage(1);

        return this;
    }

    public AppBuilder addUserProfileUseCase() {

        UserProfileOutputBoundary presenter =
                new UserProfilePresenter(userProfileViewModel, viewManagerModel);

        UserProfileInputBoundary interactor =
                new UserProfileInteractor(userDataAccessObject, presenter, userFactory, gameDashboard);

        UserProfileController controller = new UserProfileController(interactor);

        userProfileView.setUserProfileController(controller);
        return this;
    }


    // ========== Build Application ==========

    public JFrame build() {
        final JFrame application = new JFrame("Pokénator Dashboard");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        application.setSize(1200, 1000);
        application.setLocationRelativeTo(null); // Center window on screen

        // Start on Game Dashboard
        viewManagerModel.setState("dashboard");
        viewManagerModel.firePropertyChange();

        return application;
    }

}
