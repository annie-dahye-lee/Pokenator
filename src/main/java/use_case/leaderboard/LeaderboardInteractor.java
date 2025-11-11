package use_case.leaderboard;

public class LeaderboardInteractor implements LeaderboardInputBoundary {

    private final ProfilesDataAccessInterface profilesDAO;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(ProfilesDataAccessInterface profilesDAO, LeaderboardOutputBoundary leaderboardPresenter) {
        this.profilesDAO = profilesDAO;
        this.leaderboardPresenter = leaderboardPresenter;
    }

    @Override
    public void execute(LeaderboardInputData leaderboardInputData) {

    }

}
