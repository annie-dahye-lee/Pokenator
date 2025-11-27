package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import use_case.settings.AccessSettingsOutputBoundary;

/**
 * Presenter responsible for navigating from the settings screen
 * to a target view, in this case the dashboard.
 */
public class AccessSettingsPresenter implements AccessSettingsOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final SettingsViewModel settingsViewModel;
    private final String targetView;

    /**
     * Constructs an AccessSettingsPresenter.
     *
     * @param viewManagerModel  the view manager used to switch views
     * @param settingsViewModel the settings view model whose state may be cleared
     * @param targetView        the name of the view to navigate to
     */
    public AccessSettingsPresenter(ViewManagerModel viewManagerModel,
                                   SettingsViewModel settingsViewModel,
                                   String targetView) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.targetView = targetView;
    }

    /**
     * Prepares the success view by clearing any existing errors and switching
     * to the target view.
     */
    @Override
    public void prepareSuccessView() {
        // clear errors, there's none now but using the
        // close for modification open to extensions principle when errors are added later
        SettingsState state = settingsViewModel.getState();
        state.setErrorMessage(null);
        settingsViewModel.firePropertyChange();

        // switch to the target view
        viewManagerModel.setState(targetView);
        viewManagerModel.firePropertyChange();
    }

}
