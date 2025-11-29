package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Leaderboard use case interactor.
 */
public class LeaderboardInteractor implements LeaderboardInputBoundary {

    private static final int USERS_PER_PAGE = 5;

    private final UserListDataAccessInterface userListDAO;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(UserListDataAccessInterface profilesDAO, LeaderboardOutputBoundary leaderboardPresenter) {
        this.userListDAO = profilesDAO;
        this.leaderboardPresenter = leaderboardPresenter;
    }

    @Override
    public void changePage(ChangePageInputData changePageInputData) {

        // Fetch the list of user objects.
        ArrayList<User> userList = userListDAO.getUserList();

        // Get the new page number with 0-indexing.
        int newPageIndex = changePageInputData.getNewPage() - 1;

        // If the new page is not within the user list range, throw error & terminate.
        if (! verifyPage(userList, newPageIndex)) {
            String error = "New page is not within the user list range:\n" +
                    "New 0-indexed page:" + newPageIndex + " → required lower bound: " +(newPageIndex * USERS_PER_PAGE) + "\n" +
                    "Current user list length: " + userList.size();

            leaderboardPresenter.changePagePrepareFailedView(error);
            return;
        }

        // Sort the user list with the custom comparator.
        userList.sort(new UserRankingComparator());

        // Get the sublist of user-rank pairs to display.
        ArrayList<Object[]> userRankPairs = getUserRankPairs(
                userList, newPageIndex
        );

        // Update the presenter.
        leaderboardPresenter.changePagePrepareSuccessView(
                new ChangePageOutputData(userRankPairs, newPageIndex + 1)
        );
    }

    // Secondary helpers:

    public static int getUSERS_PER_PAGE() { return USERS_PER_PAGE; }

    /**
     * Return whether the new page is within the user list range.
     * Page 1 always counts, even if the list is empty.
     * @param userList full list of user objects.
     * @param newPageIndex new page number with 0-indexing.
     * @return T if the new page is within the user list range, F otherwise.
     */
    private boolean verifyPage(ArrayList<User> userList, int newPageIndex) {
        return
                newPageIndex == 0 ||

                0 < newPageIndex &&
                        newPageIndex * USERS_PER_PAGE < userList.size();
    }

    /**
     * Get the sublist of users to display for the current page paired with their ranks.
     * Precondition: verifyPage(userList, page) == true
     * @param userList full list of user objects.
     * @param newPageIndex new page number with 0-indexing.
     * @return sublist of user-rank pairs to display.
     */
    private ArrayList<Object[]> getUserRankPairs(ArrayList<User> userList, int newPageIndex) {
        // Get the lower of either <USERS_PER_PAGE> entries after the current page or the upper bound of the user list.
        int upperBound = Math.min(
                (newPageIndex + 1) * USERS_PER_PAGE,
                userList.size()
        );

        // Initialise the list of user-rank pairs.
        ArrayList<Object[]> userRankPairs = new ArrayList<>();
        int currRank = newPageIndex * USERS_PER_PAGE + 1;

        // Loop through the sublist of users to display.
        // Add the user-rank pair to the list.
        for (User currUser : userList.subList(newPageIndex * USERS_PER_PAGE, upperBound) ) {
            userRankPairs.add(
                    new Object[]{ currUser, currRank }
            );

            currRank++;
        }

        return userRankPairs;
    }

    /**
     * Comparator interface used to sort the user list.
     * First, sort by score from MOST TO LEAST.
     * If users have the same score, sort by name alphabetically.
     * Order will be absolute since names are unique.
     */
    private static class UserRankingComparator implements Comparator<User> {
        public int compare(User u1, User u2) {

            // Sort by score:
            int score1 = u1.getScore();
            int score2 = u2.getScore();

            // If u1 has LESS score than u2, then u2 appears first.
            if ( score1 < score2 ) {
                return 1;
            }

            // If u1 has MORE score than u2, then u1 appears first.
            if ( score1 > score2 ) {
                return -1;
            }

            // Otherwise, they have the same score, so sort by name.
            return u1.getName().compareTo(u2.getName());
        }
    }

}
