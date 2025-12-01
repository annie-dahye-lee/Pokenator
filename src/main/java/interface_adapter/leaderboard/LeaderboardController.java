package interface_adapter.leaderboard;

import use_case.leaderboard.*;

public class LeaderboardController {

    private final LeaderboardInputBoundary leaderboardInteractor;

    public LeaderboardController(LeaderboardInputBoundary leaderboardInteractor) {
        this.leaderboardInteractor = leaderboardInteractor;
    }

    public void changePage(int newPage) {
        leaderboardInteractor.changePage(
                new LeaderboardInputData(newPage)
        );
    }
}
