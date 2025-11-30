package use_case.leaderboard;

/**
 * The output boundary for the Leaderboard use case.
 */
public interface LeaderboardOutputBoundary {

    void prepareSuccessView(LeaderboardOutputData leaderboardOutputData);

    void prepareFailedView(String errorMessage);

}
