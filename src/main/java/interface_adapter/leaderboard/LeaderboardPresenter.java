package interface_adapter.leaderboard;

import interface_adapter.ViewManagerModel;
import use_case.leaderboard.*;
import view.GameDashboard;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final GameDashboard gameDashboard;

    public leaderboardPresenter(
            LeaderboardViewModel leaderboardViewModel,
            ViewManagerModel viewManagerModel,
            GameDashboard gameDashboard
    ) {
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;
        this.gameDashboard = gameDashboard;
    }

    @Override
    public void prepareSuccessView(LeaderboardOutputData userList) {

    }

    @Override
    public void prepareFailedView(String error) {

    }

}
