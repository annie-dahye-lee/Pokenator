package use_case.customization.settings;

public class AccessSettingsInteractor implements AccessSettingsInputBoundary {
    private final AccessSettingsOutputBoundary presenter;

    public AccessSettingsInteractor(AccessSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        try {
            presenter.prepareSuccessView();

        } catch (Exception e) {
            presenter.prepareFailView("Unable to leave settings.");
        }
    }
}
