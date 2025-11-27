package use_case.settings;

/**
 * Access boundary for the Access Settings use case.
 */
public interface AccessSettingsOutputBoundary {
    /**
     * Prepares the view the user should see after successfully requesting
     * to access the Settings screen.
     */
    void prepareSuccessView();
}
