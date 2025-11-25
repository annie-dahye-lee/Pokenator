package use_case.settings;
import interface_adapter.settings.SettingsState;

/**
 * Defines the contract for persistence operations related to game settings.
 * This is the Gateway interface in Clean Architecture.
 */
public interface SettingsDataAccessInterface {

    /**
     * Loads the user settings.
     *
     * @return the SettingsState currently stored
     */
    SettingsState loadSettings();

    /**
     * Applies the settings to the view.
     *
     * @param settingsState the settings data to store
     */
    void saveSettings(SettingsState settingsState);
}
