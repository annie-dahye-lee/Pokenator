package use_case.customization.settings;

public class ResetSettingsOutputData {
    private final String defaultTheme;

    public ResetSettingsOutputData(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    public String getDefaultTheme() {
        return defaultTheme;
    }
}
