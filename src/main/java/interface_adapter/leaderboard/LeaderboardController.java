package interface_adapter.leaderboard;

import use_case.leaderboard.*;

/**
 * The controller for the Leaderboard use case.
 */
public class LeaderboardController {

    private final LeaderboardInputBoundary leaderboardInteractor;

    public LeaderboardController(LeaderboardInputBoundary leaderboardInteractor) {
        this.leaderboardInteractor = leaderboardInteractor;
    }

    /**
     * Changes the page of users on the leaderboard for navigation.
     * @param newPage the new page number
     */
    public void changePage(int newPage) {
        leaderboardInteractor.changePage(
                new LeaderboardInputData(newPage)
        );
    }
}
