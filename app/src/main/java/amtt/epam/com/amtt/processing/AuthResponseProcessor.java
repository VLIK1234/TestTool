package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;

import java.io.InputStream;
import java.io.InputStreamReader;

import amtt.epam.com.amtt.bo.auth.AuthResponse;
import amtt.epam.com.amtt.bo.auth.AuthResponseDeserializer;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthResponseProcessor extends ResponseProcessor {

    public static String ENCODING = "UTF-8";

    @Override
    public String process(HttpResponse httpResponse) throws Exception {
        InputStream content = null;
        InputStreamReader inputStreamReader = null;
        try {
            content = httpResponse.getEntity().getContent();
            inputStreamReader = new InputStreamReader(content, ENCODING);
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
