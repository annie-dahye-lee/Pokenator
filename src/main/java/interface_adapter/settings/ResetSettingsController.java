package interface_adapter.settings;

import use_case.settings.ResetSettingsInputBoundary;

/**
 * Controller for reset application settings.
 */
public class ResetSettingsController {
    private final ResetSettingsInputBoundary resetSettingsInteractor;

    public ResetSettingsController(ResetSettingsInputBoundary resetSettingsInteractor) {
        this.resetSettingsInteractor = resetSettingsInteractor;
    }

    /**
     * Executes the reset settings use case.
     */
    public void execute() {
        resetSettingsInteractor.execute();
    }
}
