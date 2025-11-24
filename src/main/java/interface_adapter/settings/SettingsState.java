package interface_adapter.settings;

public class SettingsState {
    private String theme = "light";  // or "dark"
    private String errorMessage = null;

    public SettingsState() {}

    // Copy constructor (to be safe)
    public SettingsState(SettingsState copy) {
        this.theme = copy.theme;
        this.errorMessage = copy.errorMessage;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
