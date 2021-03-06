package amtt.epam.com.amtt.exception;

import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.util.NetUtil;

/**
 * Constants containing information for error dialog
 * Created by Artsiom_Kaliaha on 28.04.2015.
 */
public enum ExceptionType {

    AUTH(R.string.error_title_auth, R.string.error_message_auth),
    AUTH_FORBIDDEN(R.string.error_title_auth, R.string.error_message_auth_forbidden),
    NO_INTERNET(R.string.error_title_request, R.string.error_message_no_internet),
    HOST(R.string.error_title_request, R.string.error_message_host),
    BAD_GATEWAY(R.string.error_title_request, R.string.error_message_gateway),
    NOT_FOUND(R.string.error_title_request, R.string.error_message_web_address),
    INTERNET_CONNECTION_VARIATION(R.string.error_title_connection, R.string.error_message_connection),
    SSL_EXCEPTION(R.string.error_title_connection, R.string.error_message_connection);

    private final static Map<String, ExceptionType> sExceptionsMap;
    private final static Map<Integer, ExceptionType> sStatusCodeMap;

    private final int mTitle;
    private final int mMessage;

    static {
        sExceptionsMap = new HashMap<>();
        sExceptionsMap.put(AuthenticationException.class.getName(), AUTH);
        sExceptionsMap.put(JsonSyntaxException.class.getName(), AUTH);
        sExceptionsMap.put(IllegalStateException.class.getName(), NOT_FOUND);
        sExceptionsMap.put(IllegalArgumentException.class.getName(), NO_INTERNET);
        sExceptionsMap.put(UnknownHostException.class.getName(), ExceptionType.NOT_FOUND);
        sExceptionsMap.put(ConnectException.class.getName(), ExceptionType.NO_INTERNET);
        sExceptionsMap.put(ConnectTimeoutException.class.getName(), NO_INTERNET);
        sExceptionsMap.put(HttpHostConnectException.class.getName(), HOST);
        sExceptionsMap.put(SocketTimeoutException.class.getName(), INTERNET_CONNECTION_VARIATION);
        sExceptionsMap.put(javax.net.ssl.SSLPeerUnverifiedException.class.getName(), SSL_EXCEPTION);

        sStatusCodeMap = new HashMap<>();
        sStatusCodeMap.put(HttpStatus.SC_UNAUTHORIZED, AUTH);
        sStatusCodeMap.put(HttpStatus.SC_FORBIDDEN, AUTH_FORBIDDEN);
        sStatusCodeMap.put(HttpStatus.SC_BAD_GATEWAY, BAD_GATEWAY);
        sStatusCodeMap.put(HttpStatus.SC_NOT_FOUND, NOT_FOUND);
        sStatusCodeMap.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, HOST);
        sStatusCodeMap.put(HttpClient.EMPTY_STATUS_CODE, HOST);
    }

    ExceptionType(int titleId, int messageId) {
        mTitle = titleId;
        mMessage = messageId;
    }

    public int getMessage() {
        return mMessage;
    }

    public int getTitle() {
        return mTitle;
    }

    /**
     * Returns constant by exception
     */
    public static ExceptionType valueOf(Exception e) {
        if (e == null) {
            throw new IllegalArgumentException("Check AsyncTask logic. Null exception has been passed.");
        }

        String exceptionName = e.getClass().getName();
        if (e instanceof HttpException) {
            int statusCode = ((HttpException) e).getStatusCode();
            if (statusCode != HttpClient.EMPTY_STATUS_CODE) {
                return sStatusCodeMap.get(statusCode);
            }
        } else if (e instanceof UnknownHostException) {
            /*
            * Two cases:
            * 1. No internet
            * 2. Wrong web address
            * */
            if (NetUtil.isAnyConnection()) {
                return sExceptionsMap.get(exceptionName);
            } else {
                return sExceptionsMap.get(ConnectException.class.getName());
            }
        }

        if (sExceptionsMap.get(exceptionName) == null) {
            throw new NoSuchElementException("Mapping for " + exceptionName + " is undefined");
        }
        return sExceptionsMap.get(exceptionName);
    }

}
