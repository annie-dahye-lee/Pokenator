package interface_adapter.user_profile;

import interface_adapter.ViewManagerModel;
import use_case.user_profile.UserProfileOutputBoundary;
import use_case.user_profile.UserProfileOutputData;

public class UserProfilePresenter implements UserProfileOutputBoundary {
    private final UserProfileViewModel userProfileViewModel;
    private final ViewManagerModel viewManagerModel;

    public UserProfilePresenter(UserProfileViewModel userProfileViewModel,
                                ViewManagerModel viewManagerModel) {
        this.userProfileViewModel = userProfileViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(UserProfileOutputData outputData) {
        userProfileViewModel.getState().setName(outputData.getName());
        userProfileViewModel.getState().setProfilePhotoPath(outputData.getProfilePhotoPath());
        userProfileViewModel.getState().setBannerPath(outputData.getBannerPath());
        userProfileViewModel.getState().setBio(outputData.getBio());
        userProfileViewModel.getState().setFav_pokemon(outputData.getFavPokemon());
        userProfileViewModel.getState().setProfileError(null);
        userProfileViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        userProfileViewModel.getState().setProfileError(error);
        userProfileViewModel.firePropertyChange();
    }
}

