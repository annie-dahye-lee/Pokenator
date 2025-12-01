package interface_adapter.themes;

import java.awt.*;

public class GrayTheme extends Theme {

    public GrayTheme() {
        super(
                "gray",
                new Color(235, 235, 235),   // light gray background
                new Color(30, 30, 30)       // dark gray
        );
    }

}
