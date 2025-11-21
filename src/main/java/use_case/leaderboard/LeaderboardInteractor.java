package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Comparator;

public class LeaderboardInteractor implements LeaderboardInputBoundary {

    private static final int USERS_PER_PAGE = 5;

    private final UserListDataAccessInterface userListDAO;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(UserListDataAccessInterface profilesDAO, LeaderboardOutputBoundary leaderboardPresenter) {
        this.userListDAO = profilesDAO;
        this.leaderboardPresenter = leaderboardPresenter;
    }

    @Override
    public void changePage(LeaderboardInputData leaderboardInputData) {

        // Get the page # with 0-indexing.
        int newPage = leaderboardInputData.getNewPage() - 1;

        ArrayList<User> userList = userListDAO.getUserList();

        // If the new page is not within the user list range, throw error & terminate.
        if (! verifyPage(userList, newPage)) {
            leaderboardPresenter.prepareFailedView(
                    "New page is not within the user list range:\n" +
                    "New 0-indexed page:" + newPage + " → required lower bound: " +(newPage * USERS_PER_PAGE) + "\n" +
                    "Current user list length: " + userList.size());
            return;
        }

        // Sort the user list.
        userList.sort(new UserComparator());

        // Get the sublist of users to be displayed.
        ArrayList<User> currentUsers = getCurrentUsers(
                userList, newPage
        );

        // Update the presenter.
        leaderboardPresenter.prepareSuccessView(
                new LeaderboardOutputData(currentUsers, newPage + 1)
        );
    }

    public boolean verifyPage(ArrayList<User> userList, int page) {
        // Return whether the new page is within the user list range.

        return 0 <= page && page * USERS_PER_PAGE <= userList.size();
    }

    public ArrayList<User> getCurrentUsers(ArrayList<User> userList, int page) {
        // Get the users to display for the current page.
        // Precondition: verifyPage(userList, page) == true

        // Get the lower of either 5 entries after the current page or the upper bound the user list.
        int upperBound = Math.min(
                (page + 1) * USERS_PER_PAGE,
                userList.size()
        );

        return new ArrayList<>(userList.subList(
                page * USERS_PER_PAGE,
                upperBound)
        );
    }

    // Comparator interface used to sort the user list.
    // First, sort by score from most to least.
    // If users have the same score, sort by name alphabetically.
    // Order will be absolute since names are unique.
    private static class UserComparator implements Comparator<User> {
        public int compare(User u1, User u2) {

            int score1 = u1.getScore();
            int score2 = u2.getScore();

            if ( score1 < score2 ) {
                return 1;
            }
            if ( score1 > score2 ) {
                return -1;
            }

            return u1.getName().compareTo(u2.getName());
        }
    }

}
