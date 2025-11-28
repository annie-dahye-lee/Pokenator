package use_case.settings.back;


/**
 * Interactor for the "Access Settings" use case.
 * This use case is triggered when the user navigates to the Settings screen.
 * It contains no business logic because simply opening the SettingsView does not
 * require computation — it only notifies the presenter to switch the view.
 */
public class BackSettingsInteractor implements BackSettingsInputBoundary {
    private final BackSettingsOutputBoundary presenter;

    public BackSettingsInteractor(BackSettingsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Executes the use case.
     */
    @Override
    public void execute() {
        presenter.prepareSuccessView();
    }
}
