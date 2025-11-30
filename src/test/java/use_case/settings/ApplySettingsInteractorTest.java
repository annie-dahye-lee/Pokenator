package use_case.settings;

import org.junit.jupiter.api.Test;
import use_case.settings.apply.ApplySettingsInputData;
import use_case.settings.apply.ApplySettingsInteractor;
import use_case.settings.apply.ApplySettingsOutputBoundary;

import static org.junit.jupiter.api.Assertions.*;

class ApplySettingsInteractorTest {

    @Test
    void successCase() {
        // Mock presenter
        ApplySettingsOutputBoundary presenter = new ApplySettingsOutputBoundary() {
            @Override
            public void prepareSuccessView(String theme) {
                assertEquals("Dark", theme);
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not hit fail view for valid theme");
            }
        };

        ApplySettingsInteractor interactor = new ApplySettingsInteractor(presenter);
        interactor.execute(new ApplySettingsInputData("Dark"));
    }

    @Test
    void failCase_noTheme() {
        ApplySettingsOutputBoundary presenter = new ApplySettingsOutputBoundary() {
            @Override
            public void prepareSuccessView(String theme) {
                fail("Should not hit success for invalid theme");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No theme was selected.", error);
            }
        };

        ApplySettingsInteractor interactor = new ApplySettingsInteractor(presenter);
        interactor.execute(new ApplySettingsInputData(""));  // invalid
    }

    @Test
    void failCase_nullTheme() {
        ApplySettingsOutputBoundary presenter = new ApplySettingsOutputBoundary() {
            @Override
            public void prepareSuccessView(String theme) {
                fail("Should not hit success for null theme");
            }

            @Override
            public void prepareFailView(String error) {
                // Assert the expected error message
                assertEquals("No theme was selected.", error);
            }
        };

        ApplySettingsInteractor interactor = new ApplySettingsInteractor(presenter);
        // *** This is the change: Pass null into the InputData ***
        interactor.execute(new ApplySettingsInputData(null));
    }

}

