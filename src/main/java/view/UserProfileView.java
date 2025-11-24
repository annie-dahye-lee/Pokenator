package view;

import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.user_profile.UserProfileController;
import interface_adapter.user_profile.UserProfileState;
import interface_adapter.user_profile.UserProfileViewModel;

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
    private JLabel errorLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton changeBannerButton;
    private JButton changeProfilePhotoButton;

    private Image bannerImage = null;
    private Image profilePhotoImage = null;
    private String currentBannerPath = null;
    private String currentProfilePhotoPath = null;

    private UserProfileController userProfileController = null;

    public UserProfileView(UserProfileViewModel userProfileViewModel, ViewManagerModel viewManagerModel,
                          GameDashboard gameDashboard, FileUserDataAccessObject DAO) {
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

        setLayout(new BorderLayout());
        setBackground(new Color(54, 57, 63)); // Discord dark gray background

        // Create main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(54, 57, 63));

        // Banner section
        JPanel bannerPanel = createBannerPanel();
        mainPanel.add(bannerPanel);

        // Profile info section
        JPanel profileInfoPanel = createProfileInfoPanel();
        mainPanel.add(profileInfoPanel);

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createBannerPanel() {
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new OverlayLayout(bannerPanel));
        bannerPanel.setPreferredSize(new Dimension(800, 200));
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        bannerPanel.setBackground(new Color(54, 57, 63));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Banner image
        bannerLabel = new JLabel();
        bannerLabel.setAlignmentX(0.5f);
        bannerLabel.setAlignmentY(0.5f);
        bannerLabel.setPreferredSize(new Dimension(800, 200));
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
        profileInfoPanel.setBorder(BorderFactory.createEmptyBorder(80, 20, 20, 20));

        // Profile photo section (overlapping banner)
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        photoPanel.setBackground(new Color(54, 57, 63));
        photoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        // Name input section
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        namePanel.setBackground(new Color(54, 57, 63));
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Display Name:");
        nameLabel.setForeground(new Color(185, 187, 190)); // Discord text color
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setPreferredSize(new Dimension(120, 30));

        nameInputField = new JTextField(20);
        nameInputField.setBackground(new Color(48, 51, 57)); // Discord input background
        nameInputField.setForeground(new Color(219, 222, 225)); // Discord text color
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

        namePanel.add(nameLabel);
        namePanel.add(nameInputField);

        profileInfoPanel.add(namePanel);
        profileInfoPanel.add(Box.createVerticalStrut(10));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(237, 66, 69)); // Discord error red
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        profileInfoPanel.add(errorLabel);

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
        saveButton.setPreferredSize(new Dimension(120, 35));
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
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(79, 84, 92)); // Discord gray
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> {
            if (e.getSource().equals(cancelButton)) {
                errorLabel.setText(" ");
                viewManagerModel.setState("dashboard");
                viewManagerModel.firePropertyChange();
            }
        });

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

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
            } catch (IOException ex) {
                errorLabel.setText("Error loading profile photo: " + ex.getMessage());
            }
        }
    }

    private void updateBannerDisplay() {
        if (bannerImage != null) {
            Image scaledBanner = bannerImage.getScaledInstance(800, 200, Image.SCALE_SMOOTH);
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

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final UserProfileState state = (UserProfileState) evt.getNewValue();
        setFields(state);

        if (state.getProfileError() != null) {
            errorLabel.setText(state.getProfileError());
        } else {
            errorLabel.setText(" ");
        }
    }

    public void setFields(UserProfileState state) {
        nameInputField.setText(state.getName() != null ? state.getName() : "");
        
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

