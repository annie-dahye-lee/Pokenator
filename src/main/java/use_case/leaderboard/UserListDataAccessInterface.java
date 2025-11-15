package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

// DAO for the leaderboard use case.
public interface UserListDataAccessInterface {

    // Get an arraylist of all user objects in the database.
    ArrayList<User> getAllUsers();

}
