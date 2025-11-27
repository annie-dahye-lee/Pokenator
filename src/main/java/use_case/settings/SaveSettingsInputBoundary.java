package use_case.settings;

/**
 * Input boundary for the save settings use case.
 */
public interface SaveSettingsInputBoundary {
    /**
     * Executes the save-settings use case with the provided input data.
     *
     * @param inputData the theme settings chosen by the user that should be saved and applied
     */
    void execute(SaveSettingsInputData inputData);
}
