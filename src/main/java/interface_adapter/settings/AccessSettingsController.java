package interface_adapter.settings;

import use_case.customization.settings.AccessSettingsInputBoundary;

public class AccessSettingsController {
    private final AccessSettingsInputBoundary interactor;

    public AccessSettingsController(AccessSettingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
