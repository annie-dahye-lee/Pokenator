package use_case.user_profile;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import view.GameDashboard;

public class UserProfileInteractor implements UserProfileInputBoundary {

    private final UserProfileUserDataAccessInterface userDataAccessObject;
    private final UserProfileOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private GameDashboard dashboard;

    public UserProfileInteractor(UserProfileUserDataAccessInterface userDataAccessObject,
                                 UserProfileOutputBoundary userPresenter,
                                 UserFactory userFactory, GameDashboard dashboard) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
        this.userFactory = userFactory;
        this.dashboard = dashboard;
    }

    @Override
    public void execute(UserProfileInputData userProfileInputData) {
        // Validate name
        if (userProfileInputData.getName() == null || userProfileInputData.getName().trim().isEmpty()) {
            userPresenter.prepareFailView("Name cannot be empty.");
            return;
        }

        if (userProfileInputData.getName().length() > 32) {
            userPresenter.prepareFailView("Name must be <= 32 characters long.");
            return;
        }

        User u = ((FileUserDataAccessObject)userDataAccessObject).get(dashboard.getCurrentUser());
        // Keep the original username, only update display name and other profile fields
        final User user = userFactory.create(
                u.getName(), // Keep original username
                u.getPassword(),
                u.getScore(),
                userProfileInputData.getBio(),
                userProfileInputData.getFav_pokemon(),
                userProfileInputData.getProfilePhotoPath(),
                userProfileInputData.getBannerPath()
        );
        // Update the display name separately
        user.setName(userProfileInputData.getName());

        userDataAccessObject.updateUserProfile(user);

        final UserProfileOutputData userProfileOutputData = new UserProfileOutputData(
                user.getName(), 
                user.getName(), 
                user.getBio(), 
                user.getFavPokemon(),
                user.getProfilePhotoPath(),
                user.getBannerPath()
        );
        userPresenter.prepareSuccessView(userProfileOutputData);
    }
}

