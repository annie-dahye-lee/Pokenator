package use_case.customization.settings;

public interface ResetSettingsOutputBoundary {
    void prepareSuccessView(ResetSettingsOutputData outputData);
    void prepareFailView(String errorMessage);
}
