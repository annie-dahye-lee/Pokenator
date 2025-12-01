package view;

import data_access.FileUserDataAccessObject;
import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.user_profile.UserProfileController;
import interface_adapter.user_profile.UserProfileState;
import interface_adapter.user_profile.UserProfileViewModel;
import org.json.JSONArray;

import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * The View for the user profile page with Discord-style design.
 */
public class UserProfileView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "User Profile";
    private final UserProfileViewModel userProfileViewModel;
    private final ViewManagerModel viewManagerModel;
    private final GameDashboard gameDashboard;
    private final FileUserDataAccessObject DAO;

    private JLabel bannerLabel;
    private JLabel profilePhotoLabel;
    private JTextField nameInputField;
    private JTextArea bioInputField;
    private JComboBox<String> favPokemonComboBox;
    private JLabel pokemonImageLabel;
    private JLabel errorLabel;
    private JLabel successLabel;
    private JLabel profileCompletionLabel;
    private JButton saveButton;
    private JButton returnToDashboardButton;
    private JButton changeBannerButton;
    private JButton changeProfilePhotoButton;
    private PokeApiGateway pokeApiGateway;

    private Image bannerImage = null;
    private Image profilePhotoImage = null;
    private String currentBannerPath = null;
    private String currentProfilePhotoPath = null;

    private UserProfileController userProfileController = null;

    public UserProfileView(UserProfileViewModel userProfileViewModel, ViewManagerModel viewManagerModel,
                          GameDashboard gameDashboard, FileUserDataAccessObject DAO, PokeApiGateway pokeApiGateway) {
        this.userProfileViewModel = userProfileViewModel;
        this.userProfileViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && "User Profile".equals(evt.getNewValue())) {
                // Load user data when view is shown
                String currentUser = gameDashboard.getCurrentUser();
                if (currentUser != null) {
                    setFields(currentUser);
                    // Update view model state
                    var user = DAO.get(currentUser);
                    if (user != null) {
                        UserProfileState state = new UserProfileState(user);
                        userProfileViewModel.setState(state);
                    }
                }
            }
        });
        this.gameDashboard = gameDashboard;
        this.DAO = DAO;
        this.pokeApiGateway = pokeApiGateway;

        setLayout(new BorderLayout());
        setBackground(new Color(54, 57, 63)); // Discord dark gray background

        // Create main content panel with centering
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(54, 57, 63));

        // Banner section
        JPanel bannerPanel = createBannerPanel();
        bannerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(bannerPanel);

        // Profile info section
        JPanel profileInfoPanel = createProfileInfoPanel();
        profileInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(profileInfoPanel);

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonsPanel);

        // Center the main panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(54, 57, 63));
        centerPanel.add(mainPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createBannerPanel() {
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new OverlayLayout(bannerPanel));
        bannerPanel.setPreferredSize(new Dimension(900, 200));
        bannerPanel.setMaximumSize(new Dimension(900, 200));
        bannerPanel.setBackground(new Color(54, 57, 63));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Banner image
        bannerLabel = new JLabel();
        bannerLabel.setAlignmentX(0.5f);
        bannerLabel.setAlignmentY(0.5f);
        bannerLabel.setPreferredSize(new Dimension(900, 200));
        bannerLabel.setOpaque(true);
        bannerLabel.setBackground(new Color(79, 84, 92)); // Discord banner default color
        bannerLabel.setHorizontalAlignment(JLabel.CENTER);
        bannerLabel.setVerticalAlignment(JLabel.CENTER);

        // Change banner button overlay
        JPanel bannerOverlay = new JPanel();
        bannerOverlay.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bannerOverlay.setOpaque(false);
        changeBannerButton = new JButton("Change Banner");
        changeBannerButton.setBackground(new Color(88, 101, 242)); // Discord blurple
        changeBannerButton.setForeground(Color.WHITE);
        changeBannerButton.setFocusPainted(false);
        changeBannerButton.setBorderPainted(false);
        changeBannerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeBannerButton.addActionListener(e -> chooseBannerImage());
        bannerOverlay.add(changeBannerButton);

        bannerPanel.add(bannerLabel);
        bannerPanel.add(bannerOverlay);

        return bannerPanel;
    }

    private JPanel createProfileInfoPanel() {
        JPanel profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBackground(new Color(54, 57, 63));
        profileInfoPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 20, 50));
        profileInfoPanel.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

        // Profile photo section (overlapping banner) - centered
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        photoPanel.setBackground(new Color(54, 57, 63));
        photoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profilePhotoLabel = new JLabel();
        profilePhotoLabel.setPreferredSize(new Dimension(120, 120));
        profilePhotoLabel.setMinimumSize(new Dimension(120, 120));
        profilePhotoLabel.setMaximumSize(new Dimension(120, 120));
        profilePhotoLabel.setOpaque(true);
        profilePhotoLabel.setBackground(new Color(79, 84, 92));
        profilePhotoLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(54, 57, 63), 5),
                BorderFactory.createLineBorder(Color.WHITE, 3)
        ));
        profilePhotoLabel.setHorizontalAlignment(JLabel.CENTER);
        profilePhotoLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel photoButtonPanel = new JPanel();
        photoButtonPanel.setLayout(new BoxLayout(photoButtonPanel, BoxLayout.Y_AXIS));
        photoButtonPanel.setBackground(new Color(54, 57, 63));
        changeProfilePhotoButton = new JButton("Change Photo");
        changeProfilePhotoButton.setBackground(new Color(88, 101, 242));
        changeProfilePhotoButton.setForeground(Color.WHITE);
        changeProfilePhotoButton.setFocusPainted(false);
        changeProfilePhotoButton.setBorderPainted(false);
        changeProfilePhotoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeProfilePhotoButton.addActionListener(e -> chooseProfilePhoto());
        photoButtonPanel.add(changeProfilePhotoButton);

        photoPanel.add(profilePhotoLabel);
        photoPanel.add(photoButtonPanel);

        profileInfoPanel.add(photoPanel);
        profileInfoPanel.add(Box.createVerticalStrut(20));

        // Main content panel with bio on left and other fields on right
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setBackground(new Color(54, 57, 63));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

        // Left side - Bio
        JPanel bioPanel = new JPanel();
        bioPanel.setLayout(new BoxLayout(bioPanel, BoxLayout.Y_AXIS));
        bioPanel.setBackground(new Color(54, 57, 63));
        bioPanel.setPreferredSize(new Dimension(400, 250));
        bioPanel.setMaximumSize(new Dimension(400, 250));

        JLabel bioLabel = new JLabel("Bio:");
        bioLabel.setForeground(new Color(185, 187, 190));
        bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bioInputField = new JTextArea(8, 30);
        bioInputField.setBackground(new Color(48, 51, 57));
        bioInputField.setForeground(new Color(219, 222, 225));
        bioInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 34, 37), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        bioInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioInputField.setLineWrap(true);
        bioInputField.setWrapStyleWord(true);
        bioInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final UserProfileState currentState = userProfileViewModel.getState();
                currentState.setBio(bioInputField.getText());
                userProfileViewModel.setState(currentState);
                updateCompletionDisplay(currentState.getProfileCompletionPercentage());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        bioPanel.add(bioLabel);
        bioPanel.add(Box.createVerticalStrut(5));
        bioPanel.add(bioInputField);

        // Right side - Name and Favorite Pokemon
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(54, 57, 63));
        rightPanel.setPreferredSize(new Dimension(450, 250));
        rightPanel.setMaximumSize(new Dimension(450, 250));

        // Name input section
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(new Color(54, 57, 63));
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Display Name:");
        nameLabel.setForeground(new Color(185, 187, 190));
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameInputField = new JTextField(35);
        nameInputField.setBackground(new Color(48, 51, 57));
        nameInputField.setForeground(new Color(219, 222, 225));
        nameInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 34, 37), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        nameInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final UserProfileState currentState = userProfileViewModel.getState();
                currentState.setName(nameInputField.getText());
                userProfileViewModel.setState(currentState);
                updateCompletionDisplay(currentState.getProfileCompletionPercentage());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        nameInputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        namePanel.add(nameLabel);
        namePanel.add(Box.createVerticalStrut(5));
        namePanel.add(nameInputField);

        rightPanel.add(namePanel);
        rightPanel.add(Box.createVerticalStrut(20));

        // Favorite Pokemon dropdown
        JPanel pokemonPanel = new JPanel();
        pokemonPanel.setLayout(new BoxLayout(pokemonPanel, BoxLayout.Y_AXIS));
        pokemonPanel.setBackground(new Color(54, 57, 63));
        pokemonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pokemonLabel = new JLabel("Favorite Pokemon:");
        pokemonLabel.setForeground(new Color(185, 187, 190));
        pokemonLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pokemonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Horizontal panel for dropdown and view more button
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.X_AXIS));
        dropdownPanel.setBackground(new Color(54, 57, 63));
        dropdownPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Load pokemon list from gen1Pokemon.json
        java.util.ArrayList<String> pokemonList = getPokemonList();
        favPokemonComboBox = new JComboBox<>(pokemonList.toArray(new String[0]));
        favPokemonComboBox.setBackground(new Color(48, 51, 57));
        favPokemonComboBox.setForeground(new Color(219, 222, 225));
        favPokemonComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 34, 37), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        favPokemonComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        favPokemonComboBox.setPreferredSize(new Dimension(250, 35));
        favPokemonComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        favPokemonComboBox.addActionListener(e -> {
            final UserProfileState currentState = userProfileViewModel.getState();
            String selectedPokemon = (String) favPokemonComboBox.getSelectedItem();
            currentState.setFav_pokemon(selectedPokemon);
            userProfileViewModel.setState(currentState);
            updatePokemonImage(selectedPokemon);
            updateCompletionDisplay(currentState.getProfileCompletionPercentage());
        });

        // View More button
        JButton viewMoreButton = new JButton("View More");
        viewMoreButton.setBackground(new Color(88, 101, 242)); // Discord blurple
        viewMoreButton.setForeground(Color.WHITE);
        viewMoreButton.setFocusPainted(false);
        viewMoreButton.setBorderPainted(false);
        viewMoreButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        viewMoreButton.setPreferredSize(new Dimension(100, 35));
        viewMoreButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMoreButton.addActionListener(e -> {
            viewManagerModel.setState("Choose Favourite Pokemon");
            viewManagerModel.firePropertyChange();
        });

        dropdownPanel.add(favPokemonComboBox);
        dropdownPanel.add(Box.createHorizontalStrut(10));
        dropdownPanel.add(viewMoreButton);

        pokemonPanel.add(pokemonLabel);
        pokemonPanel.add(Box.createVerticalStrut(5));
        pokemonPanel.add(dropdownPanel);
        pokemonPanel.add(Box.createVerticalStrut(10));

        // Pokemon image display
        pokemonImageLabel = new JLabel();
        pokemonImageLabel.setPreferredSize(new Dimension(100, 100));
        pokemonImageLabel.setMinimumSize(new Dimension(100, 100));
        pokemonImageLabel.setMaximumSize(new Dimension(100, 100));
        pokemonImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pokemonImageLabel.setHorizontalAlignment(JLabel.CENTER);
        pokemonImageLabel.setVerticalAlignment(JLabel.CENTER);
        pokemonImageLabel.setOpaque(false);
        pokemonPanel.add(pokemonImageLabel);

        rightPanel.add(pokemonPanel);

        contentPanel.add(Box.createHorizontalStrut(20));
        contentPanel.add(bioPanel);
        contentPanel.add(Box.createHorizontalStrut(30));
        contentPanel.add(rightPanel);
        contentPanel.add(Box.createHorizontalStrut(20));

        profileInfoPanel.add(contentPanel);
        profileInfoPanel.add(Box.createVerticalStrut(10));

        // Profile completion indicator
        JPanel completionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        completionPanel.setBackground(new Color(54, 57, 63));
        completionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileCompletionLabel = new JLabel("Profile Completion: 0%");
        profileCompletionLabel.setForeground(new Color(88, 101, 242)); // Discord blurple
        profileCompletionLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        completionPanel.add(profileCompletionLabel);

        profileInfoPanel.add(completionPanel);
        profileInfoPanel.add(Box.createVerticalStrut(5));

        // Error and success labels
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        messagePanel.setBackground(new Color(54, 57, 63));
        messagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(237, 66, 69)); // Discord error red
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        successLabel = new JLabel(" ");
        successLabel.setForeground(new Color(67, 181, 129)); // Discord success green
        successLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        messagePanel.add(errorLabel);
        messagePanel.add(Box.createHorizontalStrut(10));
        messagePanel.add(successLabel);

        profileInfoPanel.add(messagePanel);

        return profileInfoPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setBackground(new Color(54, 57, 63));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(88, 101, 242)); // Discord blurple
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setMinimumSize(new Dimension(150, 40));
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> {
            if (e.getSource().equals(saveButton)) {
                final UserProfileState currentState = userProfileViewModel.getState();
                userProfileController.execute(
                        currentState.getUsername(),
                        currentState.getPassword(),
                        currentState.getScore(),
                        currentState.getBio(),
                        currentState.getFav_pokemon(),
                        currentState.getName(),
                        currentState.getProfilePhotoPath(),
                        currentState.getBannerPath()
                );
                // Show success message
                successLabel.setText("✓ Changes saved successfully!");
                errorLabel.setText(" ");
                // Clear success message after 3 seconds
                Timer timer = new Timer(3000, evt -> successLabel.setText(" "));
                timer.setRepeats(false);
                timer.start();
            }
        });

        returnToDashboardButton = new JButton("Return to Dashboard");
        returnToDashboardButton.setBackground(new Color(79, 84, 92)); // Discord gray
        returnToDashboardButton.setForeground(Color.WHITE);
        returnToDashboardButton.setFocusPainted(false);
        returnToDashboardButton.setBorderPainted(false);
        returnToDashboardButton.setPreferredSize(new Dimension(200, 40));
        returnToDashboardButton.setMinimumSize(new Dimension(200, 40));
        returnToDashboardButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        returnToDashboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnToDashboardButton.addActionListener(e -> {
            if (e.getSource().equals(returnToDashboardButton)) {
                errorLabel.setText(" ");
                successLabel.setText(" ");
                viewManagerModel.setState("dashboard");
                viewManagerModel.firePropertyChange();
            }
        });

        buttonsPanel.add(saveButton);
        buttonsPanel.add(returnToDashboardButton);

        return buttonsPanel;
    }

    private void chooseBannerImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") 
                    || name.endsWith(".png") || name.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Copy file to profiles directory
                File profilesDir = new File("images/profiles");
                if (!profilesDir.exists()) {
                    profilesDir.mkdirs();
                }

                String fileName = gameDashboard.getCurrentUser() + "_banner_" + System.currentTimeMillis() + 
                    selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                File destFile = new File(profilesDir, fileName);
                
                // Copy file
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), 
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                currentBannerPath = destFile.getPath();
                bannerImage = ImageIO.read(destFile);
                updateBannerDisplay();
                
                final UserProfileState currentState = userProfileViewModel.getState();
                currentState.setBannerPath(currentBannerPath);
                userProfileViewModel.setState(currentState);
                updateCompletionDisplay(currentState.getProfileCompletionPercentage());
            } catch (IOException ex) {
                errorLabel.setText("Error loading banner image: " + ex.getMessage());
            }
        }
    }

    private void chooseProfilePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") 
                    || name.endsWith(".png") || name.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Copy file to profiles directory
                File profilesDir = new File("images/profiles");
                if (!profilesDir.exists()) {
                    profilesDir.mkdirs();
                }

                String fileName = gameDashboard.getCurrentUser() + "_photo_" + System.currentTimeMillis() + 
                    selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                File destFile = new File(profilesDir, fileName);
                
                // Copy file
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), 
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                currentProfilePhotoPath = destFile.getPath();
                profilePhotoImage = ImageIO.read(destFile);
                updateProfilePhotoDisplay();
                
                final UserProfileState currentState = userProfileViewModel.getState();
                currentState.setProfilePhotoPath(currentProfilePhotoPath);
                userProfileViewModel.setState(currentState);
                updateCompletionDisplay(currentState.getProfileCompletionPercentage());
            } catch (IOException ex) {
                errorLabel.setText("Error loading profile photo: " + ex.getMessage());
            }
        }
    }

    private void updateBannerDisplay() {
        if (bannerImage != null) {
            Image scaledBanner = bannerImage.getScaledInstance(900, 200, Image.SCALE_SMOOTH);
            bannerLabel.setIcon(new ImageIcon(scaledBanner));
        } else {
            bannerLabel.setIcon(null);
        }
    }

    private void updateProfilePhotoDisplay() {
        if (profilePhotoImage != null) {
            Image scaledPhoto = profilePhotoImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            profilePhotoLabel.setIcon(new ImageIcon(scaledPhoto));
        } else {
            profilePhotoLabel.setIcon(null);
        }
    }

    private void updatePokemonImage(String pokemonName) {
        if (pokemonName == null || pokemonName.equals("None") || pokemonName.isEmpty()) {
            pokemonImageLabel.setIcon(null);
            return;
        }

        try {
            // Fetch Pokemon sprite from PokeAPI
            String pokemonNameLower = pokemonName.toLowerCase();
            var pokemonInfo = pokeApiGateway.fetchPokemon(pokemonNameLower);
            String spriteUrl = pokemonInfo.getSpriteUrl();
            
            if (spriteUrl != null && !spriteUrl.isEmpty()) {
                java.net.URL url = new java.net.URL(spriteUrl);
                Image pokemonImage = ImageIO.read(url);
                Image scaledImage = pokemonImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                pokemonImageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                pokemonImageLabel.setIcon(null);
            }
        } catch (Exception e) {
            // If error loading, try to load default "none" image
            try {
                File noneFile = new File("nonepokemon.jpg");
                if (noneFile.exists()) {
                    Image noneImage = ImageIO.read(noneFile);
                    Image scaledImage = noneImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    pokemonImageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    pokemonImageLabel.setIcon(null);
                }
            } catch (IOException ex) {
                pokemonImageLabel.setIcon(null);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private void updateCompletionDisplay(int percentage) {
        if (profileCompletionLabel != null) {
            profileCompletionLabel.setText("Profile Completion: " + percentage + "%");
            // Change color based on completion
            if (percentage >= 80) {
                profileCompletionLabel.setForeground(new Color(67, 181, 129)); // Green for high completion
            } else if (percentage >= 50) {
                profileCompletionLabel.setForeground(new Color(88, 101, 242)); // Blue for medium
            } else {
                profileCompletionLabel.setForeground(new Color(250, 166, 26)); // Orange for low
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final UserProfileState state = (UserProfileState) evt.getNewValue();
        setFields(state);

        if (state.getProfileError() != null) {
            errorLabel.setText(state.getProfileError());
            successLabel.setText(" ");
        } else {
            errorLabel.setText(" ");
        }
    }

    private java.util.ArrayList<String> getPokemonList() {
        java.util.ArrayList<String> pokemonList = new java.util.ArrayList<>();
        pokemonList.add("None");
        try {
            File jsonFile = new File("gen1Pokemon.json");
            if (jsonFile.exists()) {
                JSONArray json = new JSONArray(new String(Files.readAllBytes(jsonFile.toPath())));
                for (int i = 0; i < json.length(); i++) {
                    String name = json.getJSONObject(i).getString("name");
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    pokemonList.add(name);
                }
            }
        } catch (Exception e) {
            // If file doesn't exist or error reading, add some default pokemon
            pokemonList.add("Pikachu");
            pokemonList.add("Charizard");
            pokemonList.add("Blastoise");
            pokemonList.add("Venusaur");
        }
        return pokemonList;
    }

    public void setFields(UserProfileState state) {
        nameInputField.setText(state.getName() != null ? state.getName() : "");
        bioInputField.setText(state.getBio() != null ? state.getBio() : "");
        
        // Set favorite pokemon in combo box
        if (state.getFav_pokemon() != null && !state.getFav_pokemon().isEmpty()) {
            String favPokemon = state.getFav_pokemon();
            // Capitalize first letter
            if (!favPokemon.equals("None")) {
                favPokemon = Character.toUpperCase(favPokemon.charAt(0)) + favPokemon.substring(1).toLowerCase();
            }
            favPokemonComboBox.setSelectedItem(favPokemon);
            updatePokemonImage(favPokemon);
        } else {
            favPokemonComboBox.setSelectedItem("None");
            pokemonImageLabel.setIcon(null);
        }
        
        // Load banner
        if (state.getBannerPath() != null && !state.getBannerPath().isEmpty()) {
            try {
                File bannerFile = new File(state.getBannerPath());
                if (bannerFile.exists()) {
                    currentBannerPath = state.getBannerPath();
                    bannerImage = ImageIO.read(bannerFile);
                    updateBannerDisplay();
                }
            } catch (IOException e) {
                // Banner file not found or error loading
            }
        }

        // Load profile photo
        if (state.getProfilePhotoPath() != null && !state.getProfilePhotoPath().isEmpty()) {
            try {
                File photoFile = new File(state.getProfilePhotoPath());
                if (photoFile.exists()) {
                    currentProfilePhotoPath = state.getProfilePhotoPath();
                    profilePhotoImage = ImageIO.read(photoFile);
                    updateProfilePhotoDisplay();
                }
            } catch (IOException e) {
                // Photo file not found or error loading
            }
        }
        
        // Update completion display
        updateCompletionDisplay(state.getProfileCompletionPercentage());
    }

    public void setFields(String username) {
        if (username != null) {
            var user = DAO.get(username);
            if (user != null) {
                UserProfileState state = new UserProfileState(user);
                setFields(state);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setUserProfileController(UserProfileController userProfileController) {
        this.userProfileController = userProfileController;
    }
}

