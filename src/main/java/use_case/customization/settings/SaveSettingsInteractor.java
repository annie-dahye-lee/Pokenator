package use_case.customization.settings;

public class SaveSettingsInteractor implements SaveSettingsInputBoundary {
    private final SaveSettingsOutputBoundary presenter;

    public SaveSettingsInteractor(SaveSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveSettingsInputData inputData) {

        String theme = inputData.getTheme();
        // removed validation because it was hardcoded earlier
        presenter.prepareSuccessView(theme);
    }
}
