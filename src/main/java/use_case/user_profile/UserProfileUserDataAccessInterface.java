package use_case.user_profile;

import entity.User;

/**
 * Interface for data access operations related to user profile.
 */
public interface UserProfileUserDataAccessInterface {

    /**
     * Update the user profile.
     * @param user the user with updated profile information
     */
    void updateUserProfile(User user);

    /**
     * Get a user by username.
     * @param username the username
     * @return the User object
     */
    User get(String username);
}

