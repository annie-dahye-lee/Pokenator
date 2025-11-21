package view;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.*;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.beans.*;

public class LeaderboardView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "leaderboard";

    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private LeaderboardController leaderboardController = null;

    private final JPanel leaderboardPanel = new JPanel();
    private final JPanel colHeaders = new JPanel();
    private final JLabel pageLabel = new JLabel();

    public LeaderboardView(
            LeaderboardViewModel leaderboardViewModel,
            ViewManagerModel viewManagerModel
    ) {
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;

        leaderboardViewModel.addPropertyChangeListener(this);


        // Components:

        // Back button:
        JButton backButton = new JButton("Back");

        // Leaderboard list:
        leaderboardPanel.setLayout(
                new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS)
        );

        colHeaders.add(new JLabel("Rank"));
        colHeaders.add(new JLabel("User"));
        colHeaders.add(new JLabel("Score"));

        // Bottom row — page navigations:
        JPanel navs = new JPanel(new FlowLayout());

        JButton previousButton = new JButton("<");
        pageLabel.setText(Integer.toString(
                leaderboardViewModel.getState().getPage()
        ));
        JButton nextButton = new JButton(">");

        navs.add(previousButton);
        navs.add(pageLabel);
        navs.add(nextButton);

        // Main panel:
        this.add(backButton);
        this.add(leaderboardPanel);
        this.add(navs);

        // Event connections:
        backButton.addActionListener(e -> {
            viewManagerModel.setState("dashboard");
            viewManagerModel.firePropertyChange();
        });

        previousButton.addActionListener(e -> {
            leaderboardController.changePage(
                    leaderboardViewModel.getState().getPage() - 1
            );
        });

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

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        LeaderboardState state = leaderboardViewModel.getState();

        // Update the leaderboard.
        updateLeaderboard(state.getCurrentUsers());

        // Update the page label.
        pageLabel.setText(Integer.toString(
                state.getPage()
        ));
    }

    public void updateLeaderboard(ArrayList<User> userList) {
        // Repopulate the leaderboard panel with a new user list.

        // Clear the leaderboard panel to be populated with new entries.
        leaderboardPanel.removeAll();
        leaderboardPanel.add(colHeaders);

        // New leaderboard entries panel:
        JPanel leaderboardEntries = new JPanel();
        leaderboardEntries.setLayout(
                new BoxLayout(leaderboardEntries, BoxLayout.Y_AXIS)
        );
        leaderboardPanel.add(leaderboardEntries);

        // Populate with given user list.
        for (User user : userList) {
            leaderboardEntries.add(new LeaderboardEntry(user, 1)); // TODO implement index.
        }
    }

    private class LeaderboardEntry extends JPanel {

        private final User user;

        public LeaderboardEntry(User user, int i) {
            this.user = user;

            JLabel index = new JLabel(Integer.toString(i));
            JButton username = new JButton(user.getName());
            JLabel score = new JLabel(Integer.toString(
                    user.getScore()
            ));

            this.add(index);
            this.add(username);
            this.add(score);
        }
    }

}