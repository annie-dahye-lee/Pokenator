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

        // Fetch the list of user objects.
        ArrayList<User> userList = userListDAO.getUserList();

        // Get the new page number with 0-indexing.
        int newPage = leaderboardInputData.getNewPage() - 1;

        // If the new page is not within the user list range, throw error & terminate.
        if (! verifyPage(userList, newPage)) {
            leaderboardPresenter.prepareFailedView(
                    "New page is not within the user list range:\n" +
                    "New 0-indexed page:" + newPage + " → required lower bound: " +(newPage * USERS_PER_PAGE) + "\n" +
                    "Current user list length: " + userList.size());
            return;
        }

        // Sort the user list with the custom comparator.
        userList.sort(new UserRankingComparator());

        // Get the sublist of user-rank pairs to display.
        ArrayList<Object[]> userRankPairs = getUserRankPairs(
                userList, newPage
        );

        // Update the presenter.
        leaderboardPresenter.prepareSuccessView(
                new LeaderboardOutputData(userRankPairs, newPage + 1)
        );
    }

    public boolean verifyPage(ArrayList<User> userList, int page) {
        // Return whether the new page is within the user list range.

        return 0 <= page && page * USERS_PER_PAGE <= userList.size();
    }

    public ArrayList<Object[]> getUserRankPairs(ArrayList<User> userList, int page) {
        // Get the sublist of users to display for the current page paired with their ranks.
        // Precondition: verifyPage(userList, page) == true

        // Get the lower of either 5 entries after the current page or the upper bound of the user list.
        int upperBound = Math.min(
                (page + 1) * USERS_PER_PAGE,
                userList.size()
        );

        // Initialise the list of user-rank pairs.
        ArrayList<Object[]> userRankPairs = new ArrayList<>();
        int currRank = page * USERS_PER_PAGE + 1;

        // Loop through the sublist of users to display.
        // Add the user-rank pair to the list.
        for (User currUser : userList.subList(page * USERS_PER_PAGE, upperBound) ) {
            userRankPairs.add(
                    new Object[]{ currUser, currRank }
            );

            currRank++;
        }

        return userRankPairs;
    }

    // Comparator interface used to sort the user list.
    // First, sort by score from MOST TO LEAST.
    // If users have the same score, sort by name alphabetically.
    // Order will be absolute since names are unique.
    private static class UserRankingComparator implements Comparator<User> {
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
