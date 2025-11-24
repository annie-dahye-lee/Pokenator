package entity;

/**
 * Factory for creating User objects.
 */
public class UserFactory {

    public User create(String name, String password) {
        return new User(name, password);
    }

    public User create(String name, String password, int score, String bio, String fav_pokemon) {
        return new User(name, password, score, bio, fav_pokemon);
    }

    public User create(String name, String password, int score, String bio, String fav_pokemon, String profilePhotoPath, String bannerPath) {
        return new User(name, password, score, bio, fav_pokemon, profilePhotoPath, bannerPath);
    }
}
