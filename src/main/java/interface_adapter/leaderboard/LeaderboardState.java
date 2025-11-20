package interface_adapter.leaderboard;

import entity.User;

import java.util.ArrayList;

public class LeaderboardState {

    private ArrayList<User> currentUsers;
    private int page;

    public LeaderboardState() {
        currentUsers = null;
        this.page = 1;
    }

    public ArrayList<User> getCurrentUsers() { return currentUsers; }
    public void setCurrentUsers(ArrayList<User> currentUsers) { this.currentUsers = currentUsers; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

}
