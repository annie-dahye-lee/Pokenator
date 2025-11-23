package view;

import data_access.FileUserDataAccessObject;
import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfileState;
import interface_adapter.edit_profile.EditProfileViewModel;

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
import java.net.URL;

/**
 * The View for when the user is customizing their profile bio.
 */
public class EditProfileView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Edit Profile";
    private final EditProfileViewModel editProfileViewModel;
    private final ViewManagerModel viewManagerModel;

    private final GameDashboard gameDashboard;

    private final JLabel usernameInfo;
    private final JTextArea bioInputField;
    private JLabel errorLabel;
    private final JButton editBio;
    private final JButton cancel;
    private final JButton editFavPokemon;
    private final JLabel favPokemonDesc;
    private String favPokemon = "None";
    private Image image = null;
    private JLabel pokeImage = new JLabel("");

    private final FileUserDataAccessObject DAO;
    private final PokeApiGateway pokeApiGateway;

    private EditProfileController editProfileController = null;

    public EditProfileView(EditProfileViewModel editProfileViewModel, ViewManagerModel viewManagerModel,
                           GameDashboard gameDashboard, FileUserDataAccessObject DAO, PokeApiGateway pokeApiGateway) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        this.DAO = DAO;
        this.pokeApiGateway = pokeApiGateway;

        final JLabel title = new JLabel("Edit Profile");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.gameDashboard = gameDashboard;
        gameDashboard.setEPV(this); //TODO: this is probably illegal fml

        // Box for inputting new bio; saves as user types
        bioInputField = new JTextArea(5, 15);
        bioInputField.setLineWrap(true);
        bioInputField.setWrapStyleWord(true);
        bioInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();
                currentState.setBio(bioInputField.getText());
                editProfileViewModel.setState(currentState);
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
        }
        );

        final LabelTextPanel bioInfo = new LabelTextPanel(new JLabel("Bio (500 words max)"), bioInputField);

        usernameInfo = new JLabel("Currently logged in: ");

        final JPanel buttons = new JPanel();
        editBio = new JButton("Save Bio");
        buttons.add(editBio);

        cancel = new JButton("Cancel");
        buttons.add(cancel);

        errorLabel = new JLabel(" ");
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button for saving bio; executes changing the user's bio
        editBio.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(editBio)) {
                            final EditProfileState currentState = editProfileViewModel.getState();

                            editProfileController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword(),
                                    currentState.getScore(),
                                    currentState.getBio(),
                                    currentState.getFav_pokemon()
                            );
                            if (currentState.getProfileError() != null) {
                                errorLabel.setText(currentState.getProfileError());
                            } else {
                                errorLabel.setText(" ");
                            }
                        }
                    }
                }
        );

        //cancel button
        cancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(cancel)) {
                            errorLabel.setText(" ");
                            viewManagerModel.setState("dashboard");
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
        );

        final JPanel pokemonDisplay = new JPanel();
        favPokemonDesc = new JLabel("\nCurrent Favourite Pokemon: " + favPokemon + "\n");
        pokemonDisplay.add(favPokemonDesc);
        pokemonDisplay.add(pokeImage);
        try {
            if (!favPokemon.equals("None")) {
                image = ImageIO.read(new URL(pokeApiGateway.fetchPokemon(favPokemon).getSpriteUrl()));
                pokeImage.setIcon(new ImageIcon(image.getScaledInstance(
                        50, 50, Image.SCALE_SMOOTH)));
            } else {
               pokeImage.setIcon(new ImageIcon(ImageIO.read(new File("nonepokemon.jpg")).
                                getScaledInstance(50,50,Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {}

        pokemonDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);

        editFavPokemon = new JButton("Choose Favourite Pokemon");
        editFavPokemon.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(editFavPokemon)) {
                            errorLabel.setText(" ");
                            viewManagerModel.setState("Choose Favourite Pokemon");
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
        );
        buttons.add(editFavPokemon);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(usernameInfo);
        this.add(bioInfo);
        this.add(bioInputField);
        this.add(errorLabel);
        this.add(pokemonDisplay);
        this.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final EditProfileState state = (EditProfileState) evt.getNewValue();
        state.setFav_pokemon(DAO.get(gameDashboard.getCurrentUser()).getFavPokemon());
        setFields(state);

        if (state.getProfileError() != null) {
            errorLabel.setText(state.getProfileError());
        }
    }

    public void setFields(EditProfileState state) {
        favPokemon = state.getFav_pokemon();
        if (favPokemon == null || favPokemon.equals("None") || favPokemon.equals("null")) {
            favPokemon = "None";
        }
        bioInputField.setText(state.getBio());
        usernameInfo.setText("Currently logged in: " + state.getUsername());
        favPokemonDesc.setText("\nCurrent Favourite Pokemon: " + favPokemon);
        try {
            if (!favPokemon.equals("None")) {
                image = ImageIO.read(new URL(pokeApiGateway.fetchPokemon(favPokemon).getSpriteUrl()));
            } else {
                image =  ImageIO.read(new File("nonepokemon.jpg"));
            }
            pokeImage.setIcon(new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        } catch (Exception e) { }
    }

    public void setFields(String name) {
        usernameInfo.setText("Currently logged in: " + name);
        usernameInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioInputField.setText(this.DAO.get(name).getBio());
        favPokemon = this.DAO.get(name).getFavPokemon();

        if (favPokemon == null || favPokemon.equals("None") || favPokemon.equals("null")) {
            favPokemon = "None";
        }

        favPokemonDesc.setText("\nCurrent Favourite Pokemon: " + favPokemon);
        try {
            if (!favPokemon.equals("None")) {
                image = ImageIO.read(new URL(pokeApiGateway.fetchPokemon(favPokemon).getSpriteUrl()));
            } else {
                image =  ImageIO.read(new File("nonepokemon.jpg"));
            }
            pokeImage.setIcon(new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        } catch (Exception e) { }
    }

    public String getViewName() {
        return viewName;
    }

    public void setEditProfileController(EditProfileController editProfileController) {
        this.editProfileController = editProfileController;
    }
}
