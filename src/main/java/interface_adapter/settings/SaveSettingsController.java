package interface_adapter.settings;

import use_case.customization.settings.SaveSettingsInputBoundary;
import use_case.customization.settings.SaveSettingsInputData;

public class SaveSettingsController {
    private final SaveSettingsInputBoundary saveSettingsUseCase;

    public SaveSettingsController(SaveSettingsInputBoundary saveSettingsUseCase) {
        this.saveSettingsUseCase = saveSettingsUseCase;
    }

    public void execute(SettingsState state) {
        SaveSettingsInputData data =
                new SaveSettingsInputData(state.getTheme());
        saveSettingsUseCase.save(data);
    }
}
