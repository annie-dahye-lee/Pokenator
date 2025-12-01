package use_case.customization.settings;
import interface_adapter.settings.SettingsState;

/**
 * Defines the contract for persistence operations related to game settings.
 * This is the Gateway interface in Clean Architecture.
 */
public interface SettingsDataAccessInterface {

    SettingsState loadSettings();

    void saveSettings(SettingsState settingsState);
}
