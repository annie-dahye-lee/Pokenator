package view;

import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.akinator.AkinatorController;
import interface_adapter.akinator.AkinatorState;
import interface_adapter.akinator.AkinatorViewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

public class AkinatorView extends JPanel implements PropertyChangeListener {

    private final String viewName = "akinator";
    private final AkinatorViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private AkinatorController controller;

    private final JLabel promptLabel = new JLabel("Press Start to begin.", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton yesButton = new JButton("Yes");
    private final JButton noButton = new JButton("No");
    private final JButton unknownButton = new JButton("I don’t know");
    private final JButton startButton = new JButton("Start");
    private final JButton resetButton = new JButton("Reset");
    private final JButton backButton = new JButton("Back to Dashboard");
    private final JPanel guessPanel = new JPanel(new BorderLayout());
    private final JLabel guessText = new JLabel("", SwingConstants.CENTER);
    private final JLabel spriteLabel = new JLabel("No artwork", SwingConstants.CENTER);
    private final JButton guessYes = new JButton("Yes!");
    private final JButton guessNo = new JButton("Nope");

    public AkinatorView(AkinatorViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 255));

        promptLabel.setFont(promptLabel.getFont().deriveFont(Font.BOLD, 22f));
        statusLabel.setForeground(Color.DARK_GRAY);

        JPanel buttonRow = new JPanel(new FlowLayout());
        buttonRow.add(yesButton);
        buttonRow.add(noButton);
        buttonRow.add(unknownButton);

        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlRow.add(startButton);
        controlRow.add(resetButton);
        controlRow.add(backButton);

        guessPanel.setBorder(BorderFactory.createTitledBorder("My guess"));
        JPanel guessButtons = new JPanel(new FlowLayout());
        guessButtons.add(guessYes);
        guessButtons.add(guessNo);
        guessPanel.add(guessText, BorderLayout.NORTH);
        guessPanel.add(spriteLabel, BorderLayout.CENTER);
        guessPanel.add(guessButtons, BorderLayout.SOUTH);
        guessPanel.setVisible(false);

        add(promptLabel, BorderLayout.NORTH);
        add(buttonRow, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        add(guessPanel, BorderLayout.EAST);
        add(controlRow, BorderLayout.PAGE_END);

        wireActions();
    }

    private void wireActions() {
        yesButton.addActionListener(e -> controller.answerYes());
        noButton.addActionListener(e -> controller.answerNo());
        unknownButton.addActionListener(e -> controller.answerUnknown());
        guessYes.addActionListener(e -> controller.confirmGuess(true));
        guessNo.addActionListener(e -> controller.confirmGuess(false));
        startButton.addActionListener(e -> controller.start());
        resetButton.addActionListener(e -> controller.reset());
        backButton.addActionListener(e -> {
            viewManagerModel.setState("dashboard");
            viewManagerModel.firePropertyChange();
        });
    }

    public void setController(AkinatorController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("error")) {
            String message = viewModel.getState().getErrorMessage();
            if (message != null && !message.isBlank()) {
                JOptionPane.showMessageDialog(this, message, "Pokénator", JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
        if (!evt.getPropertyName().equals("state")) {
            return;
        }
        AkinatorState state = (AkinatorState) evt.getNewValue();
        promptLabel.setText(state.getPrompt());
        statusLabel.setText(state.getStatus());
        boolean guessing = state.isAwaitingGuess();
        guessPanel.setVisible(state.isGuessVisible());
        guessYes.setEnabled(guessing);
        guessNo.setEnabled(guessing);
        yesButton.setEnabled(!guessing);
        noButton.setEnabled(!guessing);
        unknownButton.setEnabled(!guessing);

        if (state.getGuessInfo() != null) {
            updateGuessInfo(state.getGuessInfo());
        } else {
            guessText.setText("");
            spriteLabel.setIcon(null);
            spriteLabel.setText("No artwork");
        }
    }

    private void updateGuessInfo(PokeApiGateway.PokemonApiInfo info) {
        guessText.setText(String.format(
                "<html>Is your Pokémon <b>%s</b>?<br/>Types: %s<br/>Height: %.2fm, Weight: %.1fkg</html>",
                info.getDisplayName(),
                info.getTypes().isEmpty() ? "-" : String.join(", ", info.getTypes()),
                info.getHeightMeters(),
                info.getWeightKg()));

        if (info.getSpriteUrl() != null) {
            try {
                Image image = ImageIO.read(new URL(info.getSpriteUrl()));
                spriteLabel.setIcon(new ImageIcon(image.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                spriteLabel.setText("");
            } catch (IOException e) {
                spriteLabel.setIcon(null);
                spriteLabel.setText("Couldn’t load sprite");
            }
        } else {
            spriteLabel.setIcon(null);
            spriteLabel.setText("No sprite from API");
        }
    }

    public String getViewName() {
        return viewName;
    }
}
