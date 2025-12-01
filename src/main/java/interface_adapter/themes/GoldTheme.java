package interface_adapter.themes;

import java.awt.*;

public class GoldTheme extends Theme {

    public GoldTheme() {
        super(
                "gold",
                new Color(250, 245, 230),   // off-white
                new Color(60, 45, 30)       // dark brown
        );
    }
}
