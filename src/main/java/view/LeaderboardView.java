package view;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.*;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemeUtil;
import interface_adapter.themes.ThemedView;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.beans.*;

/**
 * The view for displaying the game leaderboard.
 */
public class LeaderboardView extends JPanel implements PropertyChangeListener, ThemedView {

    private static final String VIEW_NAME = "leaderboard";

    private final LeaderboardViewModel leaderboardViewModel;
    private LeaderboardController leaderboardController = null;

    private final JPanel leaderboardTable = new JPanel();
    private final JLabel pageLabel = new JLabel();

    public LeaderboardView(
            LeaderboardViewModel leaderboardViewModel,
            ViewManagerModel viewManagerModel, ThemeManager themeManager
    ) {
        this.leaderboardViewModel = leaderboardViewModel;
        leaderboardViewModel.addPropertyChangeListener(this);

        // Colour Theme Changer
        themeManager.registerView(this);
        applyTheme(themeManager.getActiveTheme());

        setLayout(new BorderLayout());


        // Components:

        // Back button:
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButtonPanel.add(backButton);

        add(backButtonPanel, BorderLayout.NORTH);

        // Leaderboard list:
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new GridBagLayout());
        add(leaderboardPanel, BorderLayout.CENTER);

        leaderboardTable.setLayout(
                new BoxLayout(leaderboardTable, BoxLayout.Y_AXIS)
        );
        leaderboardTable.setBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1)
        );
        leaderboardTable.setBackground(Color.WHITE);

        leaderboardPanel.add(leaderboardTable);

        // Bottom row — page navigations:
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton previousButton = new JButton("<");
        pageLabel.setText(
                Integer.toString(
                leaderboardViewModel.getState().getPage()
        ));
        JButton nextButton = new JButton(">");

        navBar.add(previousButton);
        navBar.add(pageLabel);
        navBar.add(nextButton);

        add(navBar, BorderLayout.SOUTH);


        // Event connections:

        // Back Button:
        backButton.addActionListener(e -> {

            // If currently not on page 1, preemptively reset to page 1.
            if (leaderboardViewModel.getState().getPage() != 1) {
                leaderboardController.changePage(1);
            }

            viewManagerModel.setState("dashboard");
            viewManagerModel.firePropertyChange();
        });

        // Previous Button:
        previousButton.addActionListener(e -> {
            leaderboardController.changePage(
                    leaderboardViewModel.getState().getPage() - 1
            );
        });

        // Next Button:
        nextButton.addActionListener(e -> {
            leaderboardController.changePage(
                    leaderboardViewModel.getState().getPage() + 1
            );
        });
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    public String getViewName() { return VIEW_NAME; }

    /**
     * Listens for property change events.
     *
     * @param e A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        LeaderboardState state = leaderboardViewModel.getState();

        // Update the leaderboard.
        updateLeaderboard(state.getUserRankPairs());

        // Update the page label.
        pageLabel.setText(Integer.toString(
                state.getPage()
        ));
    }

    private void updateLeaderboard(ArrayList<Object[]> userList) {
        // Repopulate the leaderboard panel with a new user list.

        // Clear the leaderboard panel to be populated with new entries.
        leaderboardTable.removeAll();

        // Column headers:
        JPanel header = new JPanel(
                new GridLayout(1, 3)
        );
        header.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, Color.GRAY
        ));

        header.add(new JLabel("Rank", SwingConstants.CENTER));
        header.add(new JLabel("User", SwingConstants.CENTER));
        header.add(new JLabel("Score", SwingConstants.CENTER));

        leaderboardTable.add(header);

        // Populate with given user-rank pairs.
        for (Object[] pair : userList) {
            User user = (User) pair[0];
            int rank = (int) pair[1];

            // Construct and add the row with the given user and rank.
            leaderboardTable.add(constructRow(rank, user));
        }

        leaderboardTable.revalidate();
        leaderboardTable.repaint();

        System.out.println("Successfully updated the leaderboard to page " +
                leaderboardViewModel.getState().getPage());
    }

    private JPanel constructRow(int rank, User user) {
        // Construct a leaderboard (row) with the given rank and user object.
        // Uses table format.

        // Setup:
        JPanel row = new JPanel(
                new GridLayout(1, 3)
        );
        row.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, Color.LIGHT_GRAY)
        );
        row.setBackground(Color.WHITE);

        // Add info.
        row.add(new JLabel(
                Integer.toString(rank),
                SwingConstants.CENTER)
        );
        row.add(new JLabel(user.getName()));
        row.add(new JLabel(
                Integer.toString(user.getScore()),
                SwingConstants.CENTER
        ));

        return row;
    }

    /**
     * Applies a chosen theme to the leaderboard.
     *
     * @param theme the theme to apply
     */
    public void applyTheme(Theme theme) {
        ThemeUtil.applyTheme(this, theme);
    }
}