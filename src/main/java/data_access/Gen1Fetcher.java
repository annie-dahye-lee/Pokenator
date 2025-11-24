package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Gen1Fetcher {

    public List<String> loadPokemonNames(String jsonFilePath) {
        try {
            String json = Files.readString(Path.of(jsonFilePath), StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);

            List<String> names = new ArrayList<>();
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