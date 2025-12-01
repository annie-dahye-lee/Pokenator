package interface_adapter.settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ThemeViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String theme = "Light"; // default

    public void setTheme(String theme) {
        this.theme = theme;
        pcs.firePropertyChange("theme", null, theme);
    }

    public String getTheme() {
        return theme;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
}
