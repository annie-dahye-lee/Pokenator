package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.*;
import interface_adapter.login.LoginState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditProfileView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Edit Profile";
    private final EditProfileViewModel editProfileViewModel;
    private final ViewManagerModel viewManagerModel;

    private final JLabel username;
    private final JTextField bioInputField;
    private final JButton editBio;
    private final JButton cancel;
    // private final JButton editFavPokemon;

    private EditProfileController editProfileController = null;

    public EditProfileView(EditProfileViewModel editProfileViewModel, ViewManagerModel viewManagerModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        final JLabel title = new JLabel("Edit Profile");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        bioInputField = new JTextField(editProfileViewModel.getState().getBio(),30);

        final LabelTextPanel bioInfo = new LabelTextPanel(
                new JLabel("Bio (500 words max)"), bioInputField);

        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        final JPanel buttons = new JPanel();
        editBio = new JButton("Save Bio");
        buttons.add(editBio);

        cancel = new JButton("Cancel");
        buttons.add(cancel);

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
                    }
                }
            }
        );

        cancel.addActionListener(this);

        // editFavPokemon.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        bioInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final EditProfileState currentState = editProfileViewModel.getState();
                currentState.setBio(bioInputField.getText());
                editProfileViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { }

            @Override
            public void removeUpdate(DocumentEvent e) { }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        }
        );

        this.add(title);
        this.add(usernameInfo);
        this.add(bioInfo);
        this.add(bioInputField);
        this.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final EditProfileState state = (EditProfileState) evt.getNewValue();
        //setFields(state);
        //usernameErrorField.setText(state.getLoginError());
    }

    public String getViewName() {
        return viewName;
    }

    public void setEditProfileController(EditProfileController editProfileController) {
        this.editProfileController = editProfileController;
    }
}
