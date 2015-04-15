package amtt.epam.com.amtt.authorization;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public interface AuthorizationCallback {

    void onAuthorizationResult(AuthorizationResult result, String responseMessage);

}
