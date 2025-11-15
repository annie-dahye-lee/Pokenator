package use_case.leaderboard;

public class LeaderboardInteractor implements LeaderboardInputBoundary {

    private final UserListDataAccessInterface userListDAO;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(UserListDataAccessInterface profilesDAO, LeaderboardOutputBoundary leaderboardPresenter) {
        this.userListDAO = profilesDAO;
        this.leaderboardPresenter = leaderboardPresenter;
    }

    @Override
    public void execute(LeaderboardInputData leaderboardInputData) {

    }

}
