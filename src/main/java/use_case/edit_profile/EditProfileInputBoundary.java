package use_case.edit_profile;

import use_case.change_password.ChangePasswordInputData;

/**
 * The Edit Profile Use Case.
 */
public interface EditProfileInputBoundary {

    /**
     * Execute the Edit Profile Use Case.
     * @param editProfileInputData the input data for this use case
     */
    void execute(EditProfileInputData editProfileInputData);
}
