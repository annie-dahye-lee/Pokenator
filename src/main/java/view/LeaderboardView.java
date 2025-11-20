package view;

import interface_adapter.*;
import interface_adapter.leaderboard.*;
import interface_adapter.settings.SettingsState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

public class LeaderboardView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "leaderboard";

    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;

    private LeaderboardController leaderboardController = null;

    public LeaderboardView(
            LeaderboardViewModel leaderboardViewModel,
            ViewManagerModel viewManagerModel
    ) {
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;

        // Components:
        JButton backButton = new JButton("Back to Dashboard");
        JPanel leaderboardList = new JPanel();

        // Bottom row: page navigations
        JPanel navs = new JPanel(new FlowLayout());

        JButton previousButton = new JButton("<");
        JLabel pageLabel = new JLabel(
                Integer.toString(
                leaderboardViewModel.getState().getPage()
        ));
        JButton nextButton = new JButton(">");

        navs.add(previousButton);
        navs.add(pageLabel);
        navs.add(nextButton);

        // Main panel:
        this.add(backButton);
        this.add(leaderboardList);
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

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    public String getViewName() { return VIEW_NAME; }
}
