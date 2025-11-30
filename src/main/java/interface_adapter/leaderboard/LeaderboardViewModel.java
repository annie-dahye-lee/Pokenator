package interface_adapter.leaderboard;

import interface_adapter.ViewModel;

/**
 * The view model for the Leaderboard use case.
 */
public class LeaderboardViewModel extends ViewModel<LeaderboardState> {

    public LeaderboardViewModel() {
        super("leaderboard");
        setState(new LeaderboardState());
    }
}
