package use_case.customization.settings;

public class ResetSettingsInteractor implements ResetSettingsInputBoundary {
    private final ResetSettingsOutputBoundary presenter;

    public ResetSettingsInteractor(ResetSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {

        try {
            // Default theme
            String defaultTheme = "Light";

            ResetSettingsOutputData outputData =
                    new ResetSettingsOutputData(defaultTheme);

            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Failed to reset settings.");
        }
    }
}
