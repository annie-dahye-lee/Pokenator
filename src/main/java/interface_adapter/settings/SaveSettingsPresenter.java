package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import interface_adapter.themes.ThemeManager;
import use_case.customization.settings.SaveSettingsOutputBoundary;

public class SaveSettingsPresenter implements SaveSettingsOutputBoundary {
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;
    private final ThemeManager themeManager;

    public SaveSettingsPresenter(ViewManagerModel viewManagerModel,
                                 SettingsViewModel settingsViewModel,
                                 ThemeManager themeManager) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.themeManager = themeManager;
    }

    @Override
    public void prepareSuccessView(String theme) {

        // Update settings screen
        SettingsState state = settingsViewModel.getState();
        state.setTheme(theme);
        state.setErrorMessage(null);
        settingsViewModel.firePropertyChange();

        // GLOBAL THEME UPDATE
        themeManager.setTheme(theme);

        viewManagerModel.setState("settings");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SettingsState state = settingsViewModel.getState();
        state.setErrorMessage(errorMessage);
        settingsViewModel.firePropertyChange();
    }
}