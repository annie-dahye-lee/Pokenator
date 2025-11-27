package interface_adapter.settings.back;

import use_case.settings.back.BackSettingsInputBoundary;

/**
 * Controller responsible for navigating back to the dashboard
 * from the settings screen.
 */
public class BackSettingsController {
    private final BackSettingsInputBoundary interactor;

    public BackSettingsController(BackSettingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the use case to return to the dashboard.
     */
    public void execute() {
        interactor.execute();
    }
}
