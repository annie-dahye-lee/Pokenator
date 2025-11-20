package interface_adapter.logged_in;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import use_case.edit_profile.EditProfileOutputBoundary;
import use_case.edit_profile.EditProfileOutputData;

public class EditProfilePresenter implements EditProfileOutputBoundary {
    private final EditProfileViewModel editProfileViewModel;
    private final ViewManagerModel viewManagerModel;
    //private final GameDashboard dashboard;

    public EditProfilePresenter(EditProfileViewModel editProfileViewModel,
                                ViewManagerModel viewManagerModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.viewManagerModel = viewManagerModel;
        //this.dashboard = dashboard;
    }

    @Override
    public void prepareSuccessView(EditProfileOutputData outputData) {
        editProfileViewModel.getState().setFav_pokemon(outputData.getFavPokemon());
        editProfileViewModel.getState().setBio(outputData.getBio());
        editProfileViewModel.getState().setProfileError(null);

        //viewManagerModel.setState("dashboard");
        //viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        editProfileViewModel.getState().setProfileError(error);
        // editProfileViewModel.firePropertyChange("profile");
        // idk what this is
    }

    //TODO: idek what this is for
    public void updateUserLogin(User user) {
        editProfileViewModel.getState().setBio(user.getBio());
        viewManagerModel.firePropertyChange();
    }
}
