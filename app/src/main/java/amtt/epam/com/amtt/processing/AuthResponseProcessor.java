package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.protocol.HTTP;

import java.io.InputStream;
import java.io.InputStreamReader;

import amtt.epam.com.amtt.bo.auth.AuthResponse;
import amtt.epam.com.amtt.bo.auth.AuthResponseDeserializer;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthResponseProcessor implements Processor<String, HttpEntity> {

    @Override
    public String process(HttpEntity httpEntity) throws Exception {
        InputStream content = null;
        InputStreamReader inputStreamReader = null;
        try {
            content = httpEntity.getContent();
            inputStreamReader = new InputStreamReader(content, HTTP.UTF_8);
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
