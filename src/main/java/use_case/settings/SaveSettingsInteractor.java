package use_case.settings;

/**
 * Interactor for the save settings class.
 */
public class SaveSettingsInteractor implements SaveSettingsInputBoundary {
    private final SaveSettingsOutputBoundary presenter;

    public SaveSettingsInteractor(SaveSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Executes the save-settings use case.
     *
     * @param inputData the user's colour theme to save
     */
    @Override
    public void execute(SaveSettingsInputData inputData) {

        String theme = inputData.getTheme();

        if (theme == null || theme.isEmpty()) {
            presenter.prepareFailView("No theme was selected.");
        }

        else {
            // removed validation because it was hardcoded earlier
            presenter.prepareSuccessView(theme);
        }
    }
}
