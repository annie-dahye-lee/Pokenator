package interface_adapter.settings;

import use_case.customization.settings.SaveSettingsInputBoundary;
import use_case.customization.settings.SaveSettingsInputData;

public class SaveSettingsController {
    private final SaveSettingsInputBoundary saveSettingsInteractor;

    public SaveSettingsController(SaveSettingsInputBoundary saveSettingsInteractor) {
        this.saveSettingsInteractor = saveSettingsInteractor;
    }

    public void execute(SettingsState state) {
        SaveSettingsInputData data =
                new SaveSettingsInputData(state.getTheme());
        saveSettingsInteractor.execute(data);
    }
}
