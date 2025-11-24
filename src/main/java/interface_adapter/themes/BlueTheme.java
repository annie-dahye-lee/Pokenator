package interface_adapter.themes;

import java.awt.*;

public class BlueTheme extends Theme {
    public BlueTheme() {
        super(
                "blue",
                new Color(240, 245, 255),   // light blue, background
                new Color(15, 30, 60)       // navy
        );
    }
}
