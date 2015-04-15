package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import amtt.epam.com.amtt.bo.auth.AuthResponse;
import amtt.epam.com.amtt.bo.auth.AuthResponseDeserializer;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthResponseProcessor extends ResponseProcessor {

    @Override
    public String process(HttpResponse httpResponse) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AuthResponse.class, new AuthResponseDeserializer())
                .create();
        AuthResponse authResponse = gson.fromJson(builder.toString(), AuthResponse.class);

        return authResponse.getPrettyResponse();
    }
}
