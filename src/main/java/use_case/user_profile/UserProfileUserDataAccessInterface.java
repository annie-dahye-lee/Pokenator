package use_case.user_profile;

import entity.User;

/**
 * Interface for data access operations related to user profile.
 */
public interface UserProfileUserDataAccessInterface {

    /**
     * Update the user profile.
     * 
     * @param username the username of the user to update
     * @param user     the user with updated profile information
     */
    void updateUserProfile(String username, User user);

    /**
     * Update the username of a user.
     * 
     * @param oldUsername the old username
     * @param newUsername the new username
     * @param user        the user with updated username
     */
    void updateUsername(String oldUsername, String newUsername, User user);

    /**
     * Get a user by username.
     * 
     * @param username the username
     * @return the User object
     */
    User get(String username);

    /**
     * Check if a user exists by username.
     * 
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByName(String username);
}
