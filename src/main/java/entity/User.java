package entity;

/**
 * An entity representing a user's account.
 */
public class User {

    private String name;
    private String password;

    private int score;
    private  String bio;
    private String fav_pokemon; // NAME of the pokemon

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        this.name = name;
        this.password = password;
        this.score = 0;
        this.bio = "";
        this.fav_pokemon = null;
    }

    public User(String name, String password, int score, String bio, String fav_pokemon) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        this.name = name;
        this.password = password;
        this.score = score;
        this.bio =bio;
        this.fav_pokemon = fav_pokemon;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() { return score; }

    public String getBio() { return bio; }

    public String getFavPokemon() { return fav_pokemon; }

}
