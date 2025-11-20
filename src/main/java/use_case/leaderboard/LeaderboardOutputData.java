package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

public class LeaderboardOutputData {

    private final ArrayList<User> currentUsers;
    private final int newPage;

    public LeaderboardOutputData(ArrayList<User> currentUsers, int newPage) {
        this.currentUsers = currentUsers;
        this.newPage = newPage;
    }

    public ArrayList<User> getCurrentUsers() { return currentUsers; }

    public int getNewPage() { return newPage; }
}
