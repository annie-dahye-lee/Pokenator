package use_case.leaderboard;

import java.util.ArrayList;

/**
 * The output data for the Leaderboard use case.
 */
public class LeaderboardOutputData {

    private final ArrayList<Object[]> userRankPairs;
    private final int newPage;

    public LeaderboardOutputData(ArrayList<Object[]> userRankPairs, int newPage) {
        this.userRankPairs = userRankPairs;
        this.newPage = newPage;
    }

    public ArrayList<Object[]> getUserRankPairs() { return userRankPairs; }

    public int getNewPage() { return newPage; }
}
