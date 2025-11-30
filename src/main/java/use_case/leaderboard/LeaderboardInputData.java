package use_case.leaderboard;

/**
 * The input data for the Leaderboard use case.
 */
public class LeaderboardInputData {

    private final int newPage;

    public LeaderboardInputData(int newPage) { this.newPage = newPage; }

    int getNewPage() { return newPage; }

}
