package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                // views
                .addGameDashboard()
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addAkinatorView()
                .addSettingsView()
                .addEditProfileView()
                .addChooseFavPokemonView()

                // use cases
                .addSignupUseCase()
                .addLoginUseCase()
                .addAkinatorUseCase()
                .addChangePasswordUseCase()
                .addAccessSettingsUseCase()
                .addSaveSettingsUseCase()
                .addResetSettingsUseCase()
                .addEditProfileUseCase()
                .addChooseFavPokemonUseCase()
                .build();


        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
