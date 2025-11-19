package interface_adapter.logged_in;

import interface_adapter.ViewModel;

public class EditProfileViewModel extends ViewModel<EditProfileState> {

    public EditProfileViewModel() {
        super("Edit Profile");
        setState(new EditProfileState());
    }

}
