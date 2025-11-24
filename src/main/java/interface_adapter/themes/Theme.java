package interface_adapter.themes;

import java.awt.*;

public class Theme {
    private final String name;
    private final Color background;
    private final Color foreground;

    public Theme(String name, Color background, Color foreground) {
        this.name = name;
        this.background = background;
        this.foreground = foreground;
    }

    public String getName() { return name; }
    public Color getBackground() { return background; }
    public Color getForeground() { return foreground; }
}
