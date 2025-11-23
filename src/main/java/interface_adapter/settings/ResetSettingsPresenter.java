package interface_adapter.settings;

import interface_adapter.themes.ThemeManager;
import use_case.customization.settings.ResetSettingsOutputBoundary;
import use_case.customization.settings.ResetSettingsOutputData;

public class ResetSettingsPresenter implements ResetSettingsOutputBoundary {
    private final SettingsViewModel settingsViewModel;
    private final ThemeManager themeManager;

    public ResetSettingsPresenter(SettingsViewModel settingsViewModel, ThemeManager themeManager) {
        this.themeManager = themeManager;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(ResetSettingsOutputData outputData) {

        SettingsState state = settingsViewModel.getState();

        // Apply default theme
        state.setTheme(outputData.getDefaultTheme());

        // Clear any previous error messages
        state.setErrorMessage(null);

        // Store updated state
        settingsViewModel.setState(state);

        // Notify SettingsView
        settingsViewModel.firePropertyChange();

        // Update global ThemeManager — it will apply theme to all registered views
        themeManager.setTheme(outputData.getDefaultTheme());
    }

    @Override
    public void prepareFailView(String errorMessage) {

        SettingsState state = settingsViewModel.getState();

        // Set error message so UI can display it
        state.setErrorMessage(errorMessage);

        settingsViewModel.setState(state);

        // Notify SettingsView
        settingsViewModel.firePropertyChange();
    }
}
