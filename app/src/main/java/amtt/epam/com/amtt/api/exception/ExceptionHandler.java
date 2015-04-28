package amtt.epam.com.amtt.api.exception;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;

import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.rest.RestMethod;

/**
 * Created by Artsiom_Kaliaha on 28.04.2015.
 */
public class ExceptionHandler {

    private static class ExceptionDialogBuilder {

        private AlertDialog.Builder mBuilder;

        public ExceptionDialogBuilder(Context context) {
            mBuilder = new AlertDialog.Builder(context);
        }

        public ExceptionDialogBuilder setBody(String message) {
            mBuilder.setMessage(message);
            return this;
        }

        public ExceptionDialogBuilder setPositiveButton(String text, DialogInterface.OnClickListener listener) {
            if (text != null && listener != null) {
                mBuilder.setPositiveButton(text, listener);
            }
            return this;
        }

        public ExceptionDialogBuilder setNeutralButton(String text, DialogInterface.OnClickListener listener) {
            if (text != null && listener != null) {
                mBuilder.setNeutralButton(text, listener);
            }
            return this;
        }

        public ExceptionDialogBuilder setNegativeButton() {
            mBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return this;
        }

        public void cerateAndShow() {
            mBuilder.create().show();
        }

    }

    private static class ExceptionHandlerSingletonHolder {

        private static final ExceptionHandler INSTANCE = new ExceptionHandler();

    }

    private static Map<Class, ExceptionType> mExceptionsMap;
    private static Map<Integer, ExceptionType> mStatusCodeMap;

    static {
        mExceptionsMap = new HashMap<>();
        mExceptionsMap.put(AuthenticationException.class, ExceptionType.AUTH);
        mExceptionsMap.put(IllegalStateException.class, ExceptionType.AUTH);
        mExceptionsMap.put(JsonSyntaxException.class, ExceptionType.AUTH);
        mExceptionsMap.put(UnknownHostException.class, ExceptionType.NO_INTERNET);

        mStatusCodeMap = new HashMap<>();
        mStatusCodeMap.put(HttpStatus.SC_UNAUTHORIZED, ExceptionType.AUTH);
        mStatusCodeMap.put(HttpStatus.SC_FORBIDDEN, ExceptionType.AUTH_FORBIDDEN);
        mStatusCodeMap.put(HttpStatus.SC_BAD_GATEWAY, ExceptionType.BAD_GATEWAY);
        mStatusCodeMap.put(HttpStatus.SC_NOT_FOUND, ExceptionType.NOT_FOUND);
        mStatusCodeMap.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, ExceptionType.UNKNOWN);
        mStatusCodeMap.put(RestMethod.EMPTY_STATUS_CODE, ExceptionType.UNKNOWN);
    }

    private ExceptionHandler() {
    }

    public static ExceptionHandler getInstance() {
        return ExceptionHandlerSingletonHolder.INSTANCE;
    }

    public void showDialog(final JiraException e, final Context context) {
        ExceptionType currentExceptionType;

        Class exceptionClass = e.getSuppressedOne().getClass();
        if (mExceptionsMap.get(exceptionClass) != null) {
            currentExceptionType = mExceptionsMap.get(exceptionClass);
        } else {
            currentExceptionType = mStatusCodeMap.get(e.getStatusCode());
        }

        DialogInterface.OnClickListener positiveListener = null;
        DialogInterface.OnClickListener neutralListener = null;
        if (currentExceptionType == ExceptionType.AUTH) {
            positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new JiraTask.Builder<>()
                            .setCallback((JiraCallback) context)
                            .setRestMethod(e.getRestMethod())
                            .createAndExecute();
                }
            };
            neutralListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                }
            };
        }

        new ExceptionDialogBuilder(context)
                .setBody(currentExceptionType.getMessage())
                .setPositiveButton(currentExceptionType.getPositiveText(), positiveListener)
                .setNeutralButton(currentExceptionType.getNeutralText(), neutralListener)
                .setNegativeButton()
                .cerateAndShow();
    }

}
