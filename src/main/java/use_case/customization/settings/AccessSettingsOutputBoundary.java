package use_case.customization.settings;

public interface AccessSettingsOutputBoundary {
    void prepareSuccessView();
    void prepareFailView(String errorMessage);
}
