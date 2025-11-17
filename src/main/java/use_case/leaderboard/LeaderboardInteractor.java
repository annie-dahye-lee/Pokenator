package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Comparator;

public class LeaderboardInteractor implements LeaderboardInputBoundary {

    private final UserListDataAccessInterface userListDAO;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(UserListDataAccessInterface profilesDAO, LeaderboardOutputBoundary leaderboardPresenter) {
        this.userListDAO = profilesDAO;
        this.leaderboardPresenter = leaderboardPresenter;
    }

    @Override
    public void execute(LeaderboardInputData leaderboardInputData) {

        // If the event for opening the leaderboard page was not activated, terminate.
        // I don't know how this method could be called when the event was not activated, but it's okay : )
        if (!leaderboardInputData.getFired()) {
            return;
        }

        ArrayList<User> userList = userListDAO.getUserList();

        userList.sort(new UserComparator());

        leaderboardPresenter.prepareSuccessView(new LeaderboardOutputData(userList));
    }

    // Comparator interface used to sort the user list.
    // Sorts by score first, then by name.
    // Order will be absolute since names are unique.
    private static class UserComparator implements Comparator<User> {
        public int compare(User u1, User u2) {

            int score1 = u1.getScore();
            int score2 = u2.getScore();

            if ( score1 > score2 ) {
                return 1;
            }
            if ( score1 < score2 ) {
                return -1;
            }

            return u1.getName().compareTo(u2.getName());
        }
    }

}
