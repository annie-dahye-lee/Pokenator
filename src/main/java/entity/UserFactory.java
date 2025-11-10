package entity;

/**
 * Factory for creating User objects.
 */
public class UserFactory {

    public User create(String name, String password) {
        return new User(name, password);
    }
}
