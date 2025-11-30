package interface_adapter.leaderboard;

import use_case.leaderboard.*;

/**
 * The presenter for the Leaderboard use case.
 */
public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    private final LeaderboardViewModel leaderboardViewModel;

    public LeaderboardPresenter(LeaderboardViewModel leaderboardViewModel) {
        this.leaderboardViewModel = leaderboardViewModel;
    }

    /**
     * Prepares output data from the Leaderboard use case if no errors occur.
     *
     * @param results the output data
     */
    @Override
    public void prepareSuccessView(LeaderboardOutputData results) {

        LeaderboardState state = leaderboardViewModel.getState();

        state.setUserRankPairs(results.getUserRankPairs());
        state.setPage(results.getNewPage());

        leaderboardViewModel.firePropertyChange();
    }

    /**
     * Prepares output data from the Leaderboard use case if an errors occurs.
     *
     * @param error the error message
     */
    @Override
    public void prepareFailedView(String error) {
        System.out.println(error);
    }

}
