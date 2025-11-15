package entity;

public class SimplePokemonProfile {
    private final String name;
    private final boolean starter;
    private final boolean dualType;
    private final boolean legendary;

    public SimplePokemonProfile(String name, boolean starter, boolean dualType, boolean legendary) {
        this.name = name;
        this.starter = starter;
        this.dualType = dualType;
        this.legendary = legendary;
    }

    public String getName() {
        return name;
    }

    public boolean isStarter() {
        return starter;
    }

    public boolean isDualType() {
        return dualType;
    }

    public boolean isLegendary() {
        return legendary;
    }
}
