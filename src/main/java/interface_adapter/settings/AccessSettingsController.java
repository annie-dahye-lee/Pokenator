package interface_adapter.settings;

import use_case.settings.AccessSettingsInputBoundary;

/**
 * Controller responsible for navigating back to the dashboard
 * from the settings screen.
 */
public class AccessSettingsController {
    private final AccessSettingsInputBoundary interactor;

    public AccessSettingsController(AccessSettingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the use case to return to the dashboard.
     */
    public void execute() {
        interactor.execute();
    }
}
