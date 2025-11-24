package interface_adapter.edit_profile;

import entity.User;
import interface_adapter.ViewModel;

public class EditProfileViewModel extends ViewModel<EditProfileState> {

    public EditProfileViewModel(User u) {
        super("Edit Profile");
        setState(new EditProfileState(u));
    }

}
