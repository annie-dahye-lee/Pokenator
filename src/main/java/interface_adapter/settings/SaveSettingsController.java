package interface_adapter.settings;

import use_case.settings.SaveSettingsInputBoundary;
import use_case.settings.SaveSettingsInputData;

public class SaveSettingsController {
    private final SaveSettingsInputBoundary saveSettingsInteractor;


    public SaveSettingsController(SaveSettingsInputBoundary saveSettingsInteractor) {
        this.saveSettingsInteractor = saveSettingsInteractor;
    }

    /**
     * Executes the save settings use case using the current state of the view.
     * @param state the current SettingsState from the settings view
     */
    public void execute(SettingsState state) {
        SaveSettingsInputData data =
                new SaveSettingsInputData(state.getTheme());
        saveSettingsInteractor.execute(data);
    }
}
