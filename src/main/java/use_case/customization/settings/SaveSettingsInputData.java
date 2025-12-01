package use_case.customization.settings;

public class SaveSettingsInputData {
    private final String theme;

    public SaveSettingsInputData(String theme) {
        this.theme = theme;
    }

    public String getTheme() {
        return theme;
    }
}
