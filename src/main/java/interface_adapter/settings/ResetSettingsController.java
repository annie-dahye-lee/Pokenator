package interface_adapter.settings;

import use_case.customization.settings.ResetSettingsInputBoundary;

public class ResetSettingsController {
    private final ResetSettingsInputBoundary resetSettingsInteractor;

    public ResetSettingsController(ResetSettingsInputBoundary resetSettingsInteractor) {
        this.resetSettingsInteractor = resetSettingsInteractor;
    }

    public void execute() {
        resetSettingsInteractor.execute();
    }
}
