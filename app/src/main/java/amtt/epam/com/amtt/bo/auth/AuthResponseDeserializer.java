package amtt.epam.com.amtt.bo.auth;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthResponseDeserializer implements JsonDeserializer<AuthResponse> {

    @Override
    public AuthResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String userName = jsonObject.get(AuthResponse.USER_NAME).getAsString();

        JsonObject loginInfoObject = jsonObject.getAsJsonObject(AuthResponse.LOGIN_INFO_OBJECT);
        int loginCount = loginInfoObject.get(AuthResponse.LOGIN_COUNT).getAsInt();
        String previousLoginTime = loginInfoObject.get(AuthResponse.PREVIOUS_LOGIN_TIME).getAsString();
        return new AuthResponse(userName, loginCount, previousLoginTime);
    }
}
