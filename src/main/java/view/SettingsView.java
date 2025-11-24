package view;


import interface_adapter.settings.SettingsViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.AccessSettingsController;
import interface_adapter.settings.ResetSettingsController;
import interface_adapter.settings.SaveSettingsController;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemeUtil;
import interface_adapter.themes.ThemedView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays the UI and sends user input.
 * It creates the screen with all the buttons for Dark Mode, Font selectors, Color selectors, and the Save/Apply button.
 */
public class SettingsView extends JPanel implements ActionListener, ThemedView {

    public final String viewName = "settings";

    private final SettingsViewModel settingsViewModel;

    private AccessSettingsController accessController;
    private ResetSettingsController resetController;
    private SaveSettingsController saveController;

    private final JToggleButton lightModeButton;
    private final JToggleButton darkModeButton;
    private final JButton saveButton;
    private final JButton resetButton;
    private final JButton backButton;
    private final JComboBox<String> themeSelector;

    public SettingsView(SettingsViewModel settingsViewModel,
                        ThemeManager themeManager) {

        this.settingsViewModel = settingsViewModel;


        setLayout(new BorderLayout(10, 10));

        // HEADER
        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // THEME PANEL
        JPanel themePanel = new JPanel(new GridLayout(0, 1, 0, 15));
        themePanel.setBorder(BorderFactory.createTitledBorder("Colour Theme"));

        JPanel buttonRow = new JPanel(new FlowLayout());

        ButtonGroup themeGroup = new ButtonGroup();

        lightModeButton = new JToggleButton("Light Mode");
        darkModeButton = new JToggleButton("Dark Mode");

        themeGroup.add(lightModeButton);
        themeGroup.add(darkModeButton);

        lightModeButton.addActionListener(this);
        darkModeButton.addActionListener(this);

        buttonRow.add(lightModeButton);
        buttonRow.add(darkModeButton);
        themePanel.add(buttonRow);

        // Theme selector (shows available theme names)
        themeSelector = new JComboBox<>(themeManager.getThemeNames().toArray(new String[0]));
        themeSelector.setSelectedItem(settingsViewModel.getState().getTheme());
        themePanel.add(new JLabel("All Available Themes:"));
        themePanel.add(themeSelector);

        add(themePanel, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        JPanel bottom = new JPanel(new FlowLayout());
        saveButton = new JButton("Save");
        resetButton = new JButton("Reset");
        backButton = new JButton("Back");

        saveButton.addActionListener(this);
        resetButton.addActionListener(this);
        backButton.addActionListener(this);

        bottom.add(resetButton);
        bottom.add(saveButton);
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);

        // sync initial toggle
        String initial = settingsViewModel.getState().getTheme();
        if ("dark".equalsIgnoreCase(initial)) darkModeButton.setSelected(true);
        else lightModeButton.setSelected(true);

        // Register to receive theme updates and apply current theme immediately
        themeManager.registerView(this);
    }

    // Controller injection
    public void setAccessSettingsController(AccessSettingsController accessController) {
        this.accessController = accessController;
    }

    public void setResetSettingsController(ResetSettingsController resetController) {
        this.resetController = resetController;
    }

    public void setSaveSettingsController(SaveSettingsController saveController) {
        this.saveController = saveController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == saveButton) {
            // prefer the combobox selection if set, otherwise the toggle
            String chosen = (String) themeSelector.getSelectedItem();
            if (chosen == null) {
                chosen = lightModeButton.isSelected() ? "light" : "dark";
            }
            SettingsState state = settingsViewModel.getState();
            state.setTheme(chosen.toLowerCase());
            settingsViewModel.setState(state);
            settingsViewModel.firePropertyChange();

            if (saveController != null) saveController.execute(state);

        } else if (src == resetButton) {
            if (resetController != null) resetController.execute();
        } else if (src == backButton) {
            if (accessController != null) accessController.execute();
        } else if (src == lightModeButton || src == darkModeButton) {
            // update selector to match toggle
            if (lightModeButton.isSelected()) themeSelector.setSelectedItem("light");
            else if (darkModeButton.isSelected()) themeSelector.setSelectedItem("dark");
        }
    }

    @Override
    public void applyTheme(Theme theme) {
        // ThemeUtil will recolor everything in this panel
        ThemeUtil.applyTheme(this, theme);
        // ensure toggle selection matches active theme
        String name = theme.getName();
        if ("dark".equalsIgnoreCase(name)) {
            darkModeButton.setSelected(true);
            themeSelector.setSelectedItem("dark");
        } else {
            lightModeButton.setSelected(true);
            themeSelector.setSelectedItem("light");
        }
    }

    public String getViewName() {
        return viewName;
    }
}
