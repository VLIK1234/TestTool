package amtt.epam.com.amtt.exception;

import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.util.Constants;

/**
 * Constants containing information for error dialog
 * Created by Artsiom_Kaliaha on 28.04.2015.
 */
public enum ExceptionType {


    AUTH(R.string.error_title_auth, R.string.error_message_auth, R.string.error_button_try, Constants.Dialog.EMPTY_FIELD),
    AUTH_FORBIDDEN(R.string.error_title_auth, R.string.error_message_auth_forbidden, Constants.Dialog.EMPTY_FIELD, Constants.Dialog.EMPTY_FIELD),
    NO_INTERNET(R.string.error_title_request, R.string.error_message_no_internet, R.string.error_button_try, R.string.error_button_settings),
    UNKNOWN(R.string.error_title_request, R.string.error_message_unknown, R.string.error_button_try, Constants.Dialog.EMPTY_FIELD),
    BAD_GATEWAY(R.string.error_title_request, R.string.error_message_gateway, Constants.Dialog.EMPTY_FIELD, Constants.Dialog.EMPTY_FIELD),
    NOT_FOUND(R.string.error_title_request, R.string.error_message_web_address, Constants.Dialog.EMPTY_FIELD, Constants.Dialog.EMPTY_FIELD);

    private static Map<Class, ExceptionType> mExceptionsMap;
    private static Map<Integer, ExceptionType> mStatusCodeMap;

    private int mTitle;
    private int mMessage;
    private int mPositiveText;
    private int mNeutralText;

    static {
        mExceptionsMap = new HashMap<>();
        mExceptionsMap.put(AuthenticationException.class, AUTH);
        mExceptionsMap.put(JsonSyntaxException.class, ExceptionType.AUTH);
        mExceptionsMap.put(IllegalStateException.class, NOT_FOUND);
        mExceptionsMap.put(IllegalArgumentException.class, NO_INTERNET);
        mExceptionsMap.put(UnknownHostException.class, ExceptionType.NO_INTERNET);
        mExceptionsMap.put(org.apache.http.conn.ConnectTimeoutException.class, ExceptionType.NO_INTERNET);
        mExceptionsMap.put(UnknownError.class, ExceptionType.UNKNOWN);

        mStatusCodeMap = new HashMap<>();
        mStatusCodeMap.put(HttpStatus.SC_UNAUTHORIZED, ExceptionType.AUTH);
        mStatusCodeMap.put(HttpStatus.SC_FORBIDDEN, ExceptionType.AUTH_FORBIDDEN);
        mStatusCodeMap.put(HttpStatus.SC_BAD_GATEWAY, ExceptionType.BAD_GATEWAY);
        mStatusCodeMap.put(HttpStatus.SC_NOT_FOUND, ExceptionType.NOT_FOUND);
        mStatusCodeMap.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, ExceptionType.UNKNOWN);
        mStatusCodeMap.put(HttpClient.EMPTY_STATUS_CODE, ExceptionType.UNKNOWN);
    }

    private ExceptionType(int titleId, int messageId, int positiveTextId, int neutralTextId) {
        mTitle = titleId;
        mMessage = messageId;
        mPositiveText = positiveTextId;
        mNeutralText = neutralTextId;
    }

    public int getMessage() {
        return mMessage;
    }

    public int getTitle() {
        return mTitle;
    }

    public int getPositiveText() {
        return mPositiveText;
    }

    public int getNeutralText() {
        return mNeutralText;
    }

    /**
     * Returns constant by exception
     */
    public static ExceptionType valueOf(Exception e) {
        if (e != null) {
            if (e instanceof HttpException) {
                if (((HttpException) e).getStatusCode() != HttpClient.EMPTY_STATUS_CODE) {
                    return mStatusCodeMap.get(((HttpException)e).getStatusCode());
                } else {
                    Class exceptionClass = e.getSuppressed().getClass();
                    if (mExceptionsMap.get(exceptionClass) != null) {
                        return mExceptionsMap.get(exceptionClass);
                    } else {
                        return mExceptionsMap.get(UnknownError.class);
                    }
                }
            } else {
                if (mExceptionsMap.get(e.getClass()) != null) {
                    return mExceptionsMap.get(e.getClass());
                } else {
                    return mExceptionsMap.get(UnknownError.class);
                }
            }
        } else {
            return mExceptionsMap.get(UnknownError.class);
        }
    }

}
