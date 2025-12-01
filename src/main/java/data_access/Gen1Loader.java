package data_access;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class Gen1Loader {

    public ArrayList<String> loadPokemonNames(String jsonFilePath) {
        try {
            String json = Files.readString(Path.of(jsonFilePath), StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);

            ArrayList<String> names = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                names.add(obj.getString("name"));
            }
            return names;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Pokémon JSON file", e);
        }
    }
}