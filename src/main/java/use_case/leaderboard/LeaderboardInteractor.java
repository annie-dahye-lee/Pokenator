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
    public void changePage(LeaderboardInputData leaderboardInputData) {

        ArrayList<User> userList = userListDAO.getUserList();

        userList.sort(new UserComparator());

        ArrayList<User> currentUsers = getCurrentUsers(
                userList, leaderboardInputData.getNewPage()
        );

        leaderboardPresenter.prepareSuccessView(
                new LeaderboardOutputData(currentUsers)
        );
    }

    static int PAGE_CAP = 5;
    public ArrayList<User> getCurrentUsers(ArrayList<User> userList, int page) {
        // Get the users to display for the current page.

        return new ArrayList<>(userList.subList(
                (page - 1) * PAGE_CAP,
                page * PAGE_CAP)
        );
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
