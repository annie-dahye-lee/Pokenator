package interface_adapter.edit_profile;

import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInputData;

public class EditProfileController {

    private final EditProfileInputBoundary userEditProfileUseCaseInteractor;

    public EditProfileController(EditProfileInputBoundary userEditProfileUseCaseInteractor) {
        this.userEditProfileUseCaseInteractor = userEditProfileUseCaseInteractor;
    }

    /**
     * Executes the Edit Profile Use Case.
     * @param username username whose profile will change
     * @param password user's password
     * @param score user's score
     * @param bio the new bio
     * @param fav_pokemon the new favourite pokemon
     */
    public void execute(String username, String password, int score, String bio, String fav_pokemon) {
        final EditProfileInputData editProfileInputData = new EditProfileInputData(username, password,
                                                                                   score, bio, fav_pokemon);

        userEditProfileUseCaseInteractor.execute(editProfileInputData);
    }
}
