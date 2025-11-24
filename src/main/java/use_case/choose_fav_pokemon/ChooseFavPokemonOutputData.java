package use_case.choose_fav_pokemon;

public class ChooseFavPokemonOutputData {

    private final String username;
    private final String bio;
    private final String favPokemon;

    public ChooseFavPokemonOutputData(String username, String bio, String favPokemon) {
        this.username = username;
        this.bio = bio;
        this.favPokemon = favPokemon;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() { return bio; }

    public String getFavPokemon() { return favPokemon; }
}
