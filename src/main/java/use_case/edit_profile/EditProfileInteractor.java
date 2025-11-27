package use_case.edit_profile;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import view.GameDashboard;

public class EditProfileInteractor implements EditProfileInputBoundary {

    private final EditProfileUserDataAccessInterface userDataAccessObject;
    private final EditProfileOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private GameDashboard dashboard;

    public EditProfileInteractor(EditProfileUserDataAccessInterface userDataAccessObject,
            EditProfileOutputBoundary userPresenter,
            UserFactory userFactory, GameDashboard dashboard) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
        this.userFactory = userFactory;
        this.dashboard = dashboard;
    }

    @Override
    public void execute(EditProfileInputData editProfileInputData) {
        if (editProfileInputData.getBio().length() > 500) {
            userPresenter.prepareFailView("Bio must be <= 500 characters long.");
        } else {
            User u = ((FileUserDataAccessObject) userDataAccessObject).get(dashboard.getCurrentUser());
            final User user = userFactory.create(u.getName(),
                    u.getPassword(),
                    u.getScore(),
                    editProfileInputData.getBio(),
                    u.getFavPokemon());

            userDataAccessObject.editProfile(user);

            final EditProfileOutputData editProfileOutputData = new EditProfileOutputData(user.getName(), user.getBio(),
                    user.getFavPokemon());
            userPresenter.prepareSuccessView(editProfileOutputData);
        }
    }
}
