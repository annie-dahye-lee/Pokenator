package interface_adapter.leaderboard;

import use_case.leaderboard.*;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    private final LeaderboardViewModel leaderboardViewModel;

    public LeaderboardPresenter(LeaderboardViewModel leaderboardViewModel) {
        this.leaderboardViewModel = leaderboardViewModel;
    }

    @Override
    public void prepareSuccessView(LeaderboardOutputData results) {

        LeaderboardState state = leaderboardViewModel.getState();

        state.setUserRankPairs(results.getUserRankPairs());
        state.setPage(results.getNewPage());

        leaderboardViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailedView(String error) {
        System.out.println(error);
    }

}
