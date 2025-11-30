package use_case.settings.reset;

public interface ResetSettingsOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData information about the theme they want it to be
     */
    void prepareSuccessView(ResetSettingsOutputData outputData);
}
