package use_case.customization.settings;

public class SaveSettingsInteractor implements SaveSettingsInputBoundary {
    private final SaveSettingsOutputBoundary presenter;

    public SaveSettingsInteractor(SaveSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void save(SaveSettingsInputData inputData) {

        String theme = inputData.getTheme();

        // Simple validation
        if (!theme.equals("Light") && !theme.equals("Dark")) {
            presenter.prepareFailView("Invalid theme selection.");
            return;
        }

        // If valid → tell presenter to update view model
        presenter.prepareSuccessView(theme);
    }
}
