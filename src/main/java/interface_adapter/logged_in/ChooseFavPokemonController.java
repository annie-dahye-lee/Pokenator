package interface_adapter.logged_in;

import use_case.edit_profile.ChooseFavPokemonInputBoundary;
import use_case.edit_profile.ChooseFavPokemonInputData;
import use_case.edit_profile.EditProfileInputData;

public class ChooseFavPokemonController {
    private final ChooseFavPokemonInputBoundary chooseFavPokemonInteractor;

    public ChooseFavPokemonController(ChooseFavPokemonInputBoundary chooseFavPokemonInteractor) {
        this.chooseFavPokemonInteractor = chooseFavPokemonInteractor;
    }

    /**
     * Executes the Choose Favourite Pokemon Use Case.
     * @param username username whose profile will change
     * @param password user's password
     * @param score user's score
     * @param bio the new bio
     * @param fav_pokemon the new favourite pokemon
     */
    public void execute(String username, String password, int score, String bio, String fav_pokemon) {
        final ChooseFavPokemonInputData chooseFavPokemonInputData = new ChooseFavPokemonInputData(username, password,
                score, bio, fav_pokemon);

        chooseFavPokemonInteractor.execute(chooseFavPokemonInputData);
    }
}
