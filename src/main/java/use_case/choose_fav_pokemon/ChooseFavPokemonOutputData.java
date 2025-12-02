package use_case.choose_fav_pokemon;

/**
 * The output data for the Choose Favourite Pokémon use case.
 */
public class ChooseFavPokemonOutputData {

    private final String favPokemon;

    public ChooseFavPokemonOutputData(String favPokemon) {
        this.favPokemon = favPokemon;
    }

    public String getFavPokemon() { return favPokemon; }
}
