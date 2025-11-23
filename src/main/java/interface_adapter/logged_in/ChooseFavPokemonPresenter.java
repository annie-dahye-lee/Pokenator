package interface_adapter.logged_in;

import entity.User;
import interface_adapter.ViewManagerModel;
import use_case.edit_profile.ChooseFavPokemonOutputBoundary;
import use_case.edit_profile.ChooseFavPokemonOutputData;

public class ChooseFavPokemonPresenter implements ChooseFavPokemonOutputBoundary {
    private final ChooseFavPokemonViewModel chooseFavPokemonViewModel;
    private final ViewManagerModel viewManagerModel;
    //private final GameDashboard dashboard;

    public ChooseFavPokemonPresenter(ChooseFavPokemonViewModel chooseFavPokemonViewModel,
                                ViewManagerModel viewManagerModel) {
        this.chooseFavPokemonViewModel = chooseFavPokemonViewModel;
        this.viewManagerModel = viewManagerModel;
        //this.dashboard = dashboard;
    }

    @Override
    public void prepareSuccessView(ChooseFavPokemonOutputData outputData) {
        chooseFavPokemonViewModel.getState().setFav_pokemon(outputData.getFavPokemon());
        chooseFavPokemonViewModel.getState().setBio(outputData.getBio());
        chooseFavPokemonViewModel.getState().setProfileError(null);

        //viewManagerModel.setState("dashboard");
        //viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        chooseFavPokemonViewModel.getState().setProfileError(error);
        // editProfileViewModel.firePropertyChange("profile");
        // idk what this is
    }

    public void updateUserLogin(User user) {
        chooseFavPokemonViewModel.getState().setBio(user.getBio());
        viewManagerModel.firePropertyChange();
    }
}
