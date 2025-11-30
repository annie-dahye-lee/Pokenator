package interface_adapter.leaderboard;

import java.util.ArrayList;

/**
 * The state for the Leaderboard use case.
 */
public class LeaderboardState {

    private ArrayList<Object[]> userRankPairs;
    private int page;

    public LeaderboardState() {
        userRankPairs = null;
        this.page = 1;
    }

    public ArrayList<Object[]> getUserRankPairs() { return userRankPairs; }
    public void setUserRankPairs(ArrayList<Object[]> userRankPairs) { this.userRankPairs = userRankPairs; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

}
