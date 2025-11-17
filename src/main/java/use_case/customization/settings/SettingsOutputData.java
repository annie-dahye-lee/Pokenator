package use_case.customization.settings;

import interface_adapter.settings.SettingsState;

public class SettingsOutputData {
    private final SettingsState finalState;
    private final boolean success;
    private final String message;

    public SettingsOutputData(SettingsState finalState, boolean success, String message) {
        this.finalState = finalState;
        this.success = success;
        this.message = message;
    }

    public SettingsState getFinalState() { return finalState; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
