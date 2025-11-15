package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

public class LeaderboardOutputData {

    private final ArrayList<User> userList;

    public LeaderboardOutputData(ArrayList<User> userList) { this.userList = userList; }

    public ArrayList<User> getUserList() { return userList; }
}
