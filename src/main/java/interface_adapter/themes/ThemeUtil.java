package interface_adapter.themes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ThemeUtil {

    private ThemeUtil() {}

    public static void applyTheme(Container root, Theme theme) {
        List<Component> all = getAllComponents(root);
        all.add(root);

        for (Component c : all) {

            c.setBackground(theme.getBackground());
            c.setForeground(theme.getForeground());

            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                if (jc.getBorder() instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder) jc.getBorder();
                    tb.setTitleColor(theme.getForeground());
                }
            }
        }
        root.repaint();
    }

    public static List<Component> getAllComponents(Container container) {
        List<Component> list = new ArrayList<>();
        for (Component comp : container.getComponents()) {
            list.add(comp);
            if (comp instanceof Container) {
                list.addAll(getAllComponents((Container) comp));
            }
        }
        return list;
    }
}
