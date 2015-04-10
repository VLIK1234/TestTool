package amtt.epam.com.amtt.authorization.exceptions;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthHostException extends Exception {

    @Override
    public String getMessage() {
        return "Unknown host";
    }
}
