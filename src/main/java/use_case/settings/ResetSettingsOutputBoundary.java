package use_case.settings;

public interface ResetSettingsOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData information about the theme they want it to be
     */
    void prepareSuccessView(ResetSettingsOutputData outputData);
}
