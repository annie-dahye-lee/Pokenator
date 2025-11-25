package use_case.settings;

/**
 * Output boundary for the "Save Settings" use case.
 */
public interface SaveSettingsOutputBoundary {
    /**
     * Prepares the view to display a successful save.
     *
     * @param theme the theme that was successfully saved
     */
    void prepareSuccessView(String theme);

    /**
     * Prepares the view to display an error that occurred while attempting
     * to save settings.
     *
     * @param errorMessage the error message to present to the user
     */
    void prepareFailView(String errorMessage);
}
