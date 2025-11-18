package view;

import interface_adapter.*;
import interface_adapter.leaderboard.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LeaderboardView extends JPanel {

    private final String viewName = "Leaderboard";

    private final LeaderboardController leaderboardController;
    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public LeaderboardView(
            LeaderboardController leaderboardController,
            LeaderboardViewModel leaderboardViewModel,
            ViewManagerModel viewManagerModel
    ) {
        this.leaderboardController = leaderboardController;
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;

        // Components:
        JButton backButton = new JButton("Back to Dashboard");
        JPanel leaderboardList = new JPanel();

        JButton previousButton = new JButton("<");
        JLabel pageLabel = new JLabel("1");
        JButton nextButton = new JButton(">");

        leaderboardList.add(previousButton);
        leaderboardList.add(pageLabel);
        leaderboardList.add(nextButton);

        // Event connections:
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
}
