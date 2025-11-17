package view;


import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.AccessSettingsController;
import interface_adapter.settings.ResetSettingsController;
import interface_adapter.settings.SaveSettingsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
/**
 * Displays the UI and sends user input.
 * It creates the screen with all the buttons for Dark Mode, Font selectors, Color selectors, and the Save/Apply button.
 */
public class SettingsView extends JPanel implements ActionListener, PropertyChangeListener {

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

    public SettingsView(SettingsViewModel settingsViewModel, ViewManagerModel viewManagerModel) {
        this.settingsViewModel = settingsViewModel;
        settingsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));

        // HEADER
        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // CENTER PANEL
        JPanel themePanel = new JPanel(new GridLayout(0,1,0,15));
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

        // Initial sync
        updateUIFromState(settingsViewModel.getState());
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
            saveController.execute(settingsViewModel.getState());
        }
        else if (src == resetButton) {
            resetController.execute();
        }
        else if (src == backButton) {
            accessController.execute();
        }
        else if (src == lightModeButton || src == darkModeButton) {
            SettingsState state = settingsViewModel.getState();
            if (src == lightModeButton) {
                state.setTheme("Light");
            } else {
                state.setTheme("Dark");
            }
            settingsViewModel.firePropertyChange();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SettingsState newState = (SettingsState) evt.getNewValue();
        updateUIFromState(newState);
    }

    private void updateUIFromState(SettingsState state) {
        String theme = state.getTheme();

        if ("Light".equals(theme)) {
            lightModeButton.setSelected(true);
            applyLightTheme();
        } else if ("Dark".equals(theme)) {
            darkModeButton.setSelected(true);
            applyDarkTheme();
        }

        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void applyLightTheme() {
        Color bg = Color.WHITE;
        Color fg = Color.BLACK;

        updateComponentColors(this, bg, fg);
        repaint();
    }

    private void applyDarkTheme() {
        Color bg = new Color(45, 45, 45);
        Color fg = new Color(230, 230, 230);

        updateComponentColors(this, bg, fg);
        repaint();
    }

    private void updateComponentColors(Component comp, Color bg, Color fg) {

        comp.setBackground(bg);
        comp.setForeground(fg);

        if (comp instanceof JComponent) {
            applyBorderColor((JComponent) comp, fg);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentColors(child, bg, fg);
            }
        }
    }

    private void applyBorderColor(JComponent comp, Color color) {
        if (comp.getBorder() instanceof javax.swing.border.TitledBorder) {
            javax.swing.border.TitledBorder border =
                    (javax.swing.border.TitledBorder) comp.getBorder();
            border.setTitleColor(color);
            comp.repaint();
        }
    }

    public String getViewName() {
        return viewName;
    }
}
