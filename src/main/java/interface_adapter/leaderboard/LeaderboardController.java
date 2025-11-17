package interface_adapter.leaderboard;

import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInputData;

public class LeaderboardController {

    private final LeaderboardInputBoundary leaderboardInteractor;

    public LeaderboardController(LeaderboardInputBoundary leaderboardInteractor) {
        this.leaderboardInteractor = leaderboardInteractor;
    }

    public void execute(boolean fired) {
        leaderboardInteractor.execute(new LeaderboardInputData(fired));
    }
}
