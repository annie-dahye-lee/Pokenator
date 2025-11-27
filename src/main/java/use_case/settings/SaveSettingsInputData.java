package use_case.settings;

/**
 * The DAO for the save settings use case
 */
public class SaveSettingsInputData {
    private final String theme;

    public SaveSettingsInputData(String theme) {
        this.theme = theme;
    }

    /**
     * Returns the theme selected by the user.
     *
     * @return the name of the selected theme
     */
    public String getTheme() {
        return theme;
    }
}
