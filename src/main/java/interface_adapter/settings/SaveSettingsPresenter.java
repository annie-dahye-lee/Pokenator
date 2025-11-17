package interface_adapter.settings;

import use_case.customization.settings.SaveSettingsOutputBoundary;

public class SaveSettingsPresenter implements SaveSettingsOutputBoundary {
    private final SettingsViewModel settingsViewModel;

    public SaveSettingsPresenter(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(String theme) {

        SettingsState state = settingsViewModel.getState();
        state.setTheme(theme);
        state.setErrorMessage(null);

        // Tell the view that the theme changed
        settingsViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SettingsState state = settingsViewModel.getState();
        state.setErrorMessage(errorMessage);
        settingsViewModel.firePropertyChange();
    }
}