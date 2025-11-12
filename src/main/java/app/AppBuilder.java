package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LoggedInView;
import view.GameDashboard;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import data_access.PokeApiGateway;
import interface_adapter.akinator.AkinatorController;
import interface_adapter.akinator.AkinatorPresenter;
import interface_adapter.akinator.AkinatorViewModel;
import use_case.akinator.AkinatorInputBoundary;
import use_case.akinator.AkinatorInteractor;
import use_case.akinator.AkinatorOutputBoundary;
import view.AkinatorView;

import javax.swing.*;
import java.awt.*;

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
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // ========== Add Views ==========
    private AkinatorViewModel akinatorViewModel;
    private AkinatorView akinatorView;

    public AppBuilder addGameDashboard() {
        gameDashboard = new GameDashboard(viewManagerModel); // fixed: assign to field
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
        loginView = new LoginView(loginViewModel, viewManagerModel);
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
        akinatorView = new AkinatorView(akinatorViewModel, viewManagerModel);
        cardPanel.add(akinatorView, akinatorView.getViewName());
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
        AkinatorInputBoundary interactor = new AkinatorInteractor(presenter, new PokeApiGateway());
        AkinatorController controller = new AkinatorController(interactor);
        akinatorView.setController(controller);
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
