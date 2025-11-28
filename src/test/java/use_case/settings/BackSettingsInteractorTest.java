package use_case.settings;

import org.junit.jupiter.api.Test;
import use_case.settings.back.BackSettingsInteractor;
import use_case.settings.back.BackSettingsOutputBoundary;

import static org.junit.jupiter.api.Assertions.*;

class BackSettingsInteractorTest {

    @Test
    void testAccessSettingsCallsPresenter() {
        // Arrange
        final boolean[] wasCalled = {false};

        BackSettingsOutputBoundary presenter = new BackSettingsOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                wasCalled[0] = true;
            }
        };

        BackSettingsInteractor interactor = new BackSettingsInteractor(presenter);

        // Act
        interactor.execute();

        // Assert
        assertTrue(wasCalled[0], "Presenter should be called");
    }
}
