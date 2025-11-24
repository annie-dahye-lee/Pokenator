package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class TypeFetcher {
    private final OkHttpClient client = new OkHttpClient();

    public HashMap<String, ArrayList<String>> getTypeMult(String type) throws Exception{
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        String request_url = "https://pokeapi.co/api/v2/type/" + type.toLowerCase() + "/";
        final Request request = new Request.Builder().url(request_url).build();

        try {
            final Response respoonse = client.new
        }
    }

}
