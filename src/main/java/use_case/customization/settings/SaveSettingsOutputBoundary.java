package use_case.customization.settings;

public interface SaveSettingsOutputBoundary {
    void prepareSuccessView(String theme);
    void prepareFailView(String errorMessage);
}
