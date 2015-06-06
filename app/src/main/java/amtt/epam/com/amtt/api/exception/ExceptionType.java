package amtt.epam.com.amtt.api.exception;

import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.util.Constants;

/**
 * Constants containing information for error dialog
 * Created by Artsiom_Kaliaha on 28.04.2015.
 */
public enum ExceptionType {


    AUTH(R.string.error_title_auth, R.string.error_message_auth, R.string.error_button_try, Constants.Dialog.EMPTY_FIELD),
    AUTH_FORBIDDEN(R.string.error_title_auth, R.string.error_message_auth_forbidden, Constants.Dialog.EMPTY_FIELD, Constants.Dialog.EMPTY_FIELD),
    NO_INTERNET(R.string.error_title_request, R.string.error_message_no_internet, R.string.error_button_try, R.string.error_button_settings),
    UNKNOWN(R.string.error_title_request, R.string.error_message_unknown, Constants.Dialog.EMPTY_FIELD, Constants.Dialog.EMPTY_FIELD),
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
        mExceptionsMap.put(IllegalArgumentException.class, NOT_FOUND);
        mExceptionsMap.put(UnknownHostException.class, ExceptionType.NO_INTERNET);
        mExceptionsMap.put(UnknownError.class, ExceptionType.UNKNOWN);

        mStatusCodeMap = new HashMap<>();
        mStatusCodeMap.put(HttpStatus.SC_UNAUTHORIZED, ExceptionType.AUTH);
        mStatusCodeMap.put(HttpStatus.SC_FORBIDDEN, ExceptionType.AUTH_FORBIDDEN);
        mStatusCodeMap.put(HttpStatus.SC_BAD_GATEWAY, ExceptionType.BAD_GATEWAY);
        mStatusCodeMap.put(HttpStatus.SC_NOT_FOUND, ExceptionType.NOT_FOUND);
        mStatusCodeMap.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, ExceptionType.UNKNOWN);
        mStatusCodeMap.put(RestMethod.EMPTY_STATUS_CODE, ExceptionType.UNKNOWN);
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
    public static ExceptionType valueOf(AmttException e) {
        if (e!=null) {
            if (e.getSuppressedOne() != null) {
                Class exceptionClass = e.getSuppressedOne().getClass();
                return mExceptionsMap.get(exceptionClass);
            } else {
                return mStatusCodeMap.get(e.getStatusCode());
            }
        }else{
            return mExceptionsMap.get(UnknownError.class);
        }
    }

}
