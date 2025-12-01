package interface_adapter.choose_fav_pokemon;

import entity.User;
import interface_adapter.ViewModel;

public class ChooseFavPokemonViewModel extends ViewModel<ChooseFavPokemonState> {

    public ChooseFavPokemonViewModel(User u) {
        super("Edit Profile");
        setState(new ChooseFavPokemonState(u));
    }
}
