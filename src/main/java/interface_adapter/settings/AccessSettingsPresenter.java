package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import use_case.customization.settings.AccessSettingsOutputBoundary;

public class AccessSettingsPresenter implements AccessSettingsOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final SettingsViewModel settingsViewModel;
    private final String targetView;

    public AccessSettingsPresenter(ViewManagerModel viewManagerModel,
                                   SettingsViewModel settingsViewModel,
                                   String targetView) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.targetView = targetView;
    }

    @Override
    public void prepareSuccessView() {
        // clear errors
        SettingsState state = settingsViewModel.getState();
        state.setErrorMessage(null);
        settingsViewModel.firePropertyChange();

        // switch to the target view
        viewManagerModel.setState(targetView);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SettingsState state = settingsViewModel.getState();
        state.setErrorMessage(errorMessage);
        settingsViewModel.firePropertyChange();
    }
}
