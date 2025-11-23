package interface_adapter.themes;
import java.util.*;

public class ThemeManager {
    private final Map<String, Theme> themes = new HashMap<>();
    private Theme activeTheme;

    private final List<ThemedView> registeredViews = new ArrayList<>();

    public ThemeManager() {
        registerTheme(new LightTheme());
        registerTheme(new DarkTheme());
        registerTheme(new BlueTheme());
        registerTheme(new GoldTheme());
        registerTheme(new GrayTheme());
        activeTheme = themes.get("light");
    }

    public void registerTheme(Theme theme) {
        themes.put(theme.getName(), theme);
    }

    public Set<String> getThemeNames() {
        return themes.keySet();
    }

    public void setTheme(String themeName) {
        if (themeName == null) return;
        String key = themeName.toLowerCase();
        if (themes.containsKey(key)) {
            activeTheme = themes.get(key);
            applyThemeToAllViews();
        }
    }

    public Theme getActiveTheme() {
        return activeTheme;
    }

    public void registerView(ThemedView view) {
        if (!registeredViews.contains(view)) {
            registeredViews.add(view);
        }
        // apply current theme to the view immediately
        if (activeTheme != null) view.applyTheme(activeTheme);
    }

    public void applyThemeToAllViews() {
        for (ThemedView v : registeredViews) {
            v.applyTheme(activeTheme);
        }
    }
}
