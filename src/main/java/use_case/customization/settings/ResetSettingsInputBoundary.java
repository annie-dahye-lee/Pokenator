package use_case.customization.settings;

public interface ResetSettingsInputBoundary {
    /**
     * Executes the process of resetting all settings to factory defaults.
     * The Interactor will return the newly created default state to the controller.
     * * @return The new default SettingsState.
     */
    void execute();
}
