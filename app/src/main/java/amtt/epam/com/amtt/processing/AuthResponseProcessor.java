package amtt.epam.com.amtt.processing;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import amtt.epam.com.amtt.BuildConfig;
import amtt.epam.com.amtt.bo.auth.AuthResponse;
import amtt.epam.com.amtt.bo.auth.AuthResponseDeserializer;
import amtt.epam.com.amtt.util.IOUtils;
import io.fabric.sdk.android.services.network.HttpRequest;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthResponseProcessor extends ResponseProcessor {

    @Override
    public String process(HttpResponse httpResponse) throws Exception {
        //TODO
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_NOT_FOUND) {
            //TODO use correct type of message
            throw new AuthenticationException("not found");
        } else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }

        InputStream content = null;
        InputStreamReader inputStreamReader = null;
        try {
            content = httpResponse.getEntity().getContent();
            inputStreamReader = new InputStreamReader(content, "UTF-8");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AuthResponse.class, new AuthResponseDeserializer())
                    .create();
            AuthResponse authResponse = gson.fromJson(inputStreamReader, AuthResponse.class);

            return authResponse.getPrettyResponse();
        } finally {
            IOUtils.close(content, inputStreamReader);
        }
    }
}
