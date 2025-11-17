package use_case.customization.settings;

import interface_adapter.settings.SettingsState;

/**
 * Defines the contracts for all management actions: loadSettings(), saveSettings(inputData), and resetSettings(inputData).
 */
public interface SettingsManagementInputBoundary {
    void execute(SettingsState state);
}
