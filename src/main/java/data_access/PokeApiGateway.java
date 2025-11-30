package data_access;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import okhttp3.Response;
import okhttp3.Request;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for fetching information from the PokeAPI.
 */
public class PokeApiGateway {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetches the information of a Pokémon by name.
     *
     * @param name name of the Pokémon
     * @return information on the Pokémon, including type, height, weight, and other stats
     */
    public PokemonApiInfo fetchPokemon(String name) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + name.toLowerCase())
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("PokéAPI returned " + response.code());
            }
            JSONObject json = new JSONObject(response.body().string());
            JSONObject sprites = json.optJSONObject("sprites");
            String spriteUrl = sprites != null ? sprites.optString("front_default", null) : null;

            JSONArray typesArray = json.optJSONArray("types");
            List<String> types = new ArrayList<>();
            if (typesArray != null) {
                for (int i = 0; i < typesArray.length(); i++) {
                    JSONObject typeObj = typesArray.getJSONObject(i).getJSONObject("type");
                    String typeName = typeObj.getString("name");
                    types.add(typeName.substring(0, 1).toUpperCase() + typeName.substring(1));
                }
            }

            double heightMeters = json.optDouble("height", 0) / 10.0;
            double weightKg = json.optDouble("weight", 0) / 10.0;

            return new PokemonApiInfo(
                    capitalize(name),
                    spriteUrl,
                    types,
                    heightMeters,
                    weightKg);
        }
    }


    private String capitalize(String text) {
        if (text == null || text.isBlank()) return "";
        String lower = text.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    /**
     * Information taken from the PokeAPI.
     */
    public static class PokemonApiInfo {
        private final String displayName;
        private final String spriteUrl;
        private final List<String> types;
        private final double heightMeters;
        private final double weightKg;

        public PokemonApiInfo(String displayName,
                              String spriteUrl,
                              List<String> types,
                              double heightMeters,
                              double weightKg) {
            this.displayName = displayName;
            this.spriteUrl = spriteUrl;
            this.types = types;
            this.heightMeters = heightMeters;
            this.weightKg = weightKg;
        }

        /**
         * Returns the display name of the Pokémon.
         *
         * @return the display name
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Returns the sprite URL of the Pokémon.
         *
         * @return the sprite URL
         */
        public String getSpriteUrl() {
            return spriteUrl;
        }

        /**
         * Returns the type(s) of the Pokémon.
         *
         * @return the Pokémon's type(s); one Pokémon can have several
         */
        public List<String> getTypes() {
            return types;
        }

        /**
         * Returns the height of the Pokémon.
         *
         * @return the height in meters
         */
        public double getHeightMeters() {
            return heightMeters;
        }

        /**
         * Returns the weight of the Pokémon.
         *
         * @return the weight in kgs
         */
        public double getWeightKg() {
            return weightKg;
        }
    }
}