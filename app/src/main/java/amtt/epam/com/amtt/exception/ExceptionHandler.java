package amtt.epam.com.amtt.exception;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.loader.BlockingStack;
import amtt.epam.com.amtt.util.DialogUtils;

/**
 * Common class for handling request exceptions
 * Created by Artsiom_Kaliaha on 28.04.2015.
 */
public class ExceptionHandler {

    private static class ExceptionHandlerSingletonHolder {

        private static final ExceptionHandler INSTANCE = new ExceptionHandler();

    }

    private static Map<ExceptionType, BlockingStack<Exception>> mReceivedExceptionsMap;

    private Exception mLastProcessedException;
    private ExceptionType mLastType;

    static {
        mReceivedExceptionsMap = new HashMap<>();
    }

    private ExceptionHandler() {
    }

    public static ExceptionHandler getInstance() {
        return ExceptionHandlerSingletonHolder.INSTANCE;
    }

    /**
     * Identifies exception type, adds to the failed requests stack, set the last processed exception and it's type
     */
    public ExceptionHandler processError(final Exception exception) {
        mLastProcessedException = exception;
        mLastType = ExceptionType.valueOf(exception);

        try {
            BlockingStack<Exception> exceptionStack;
            if (mReceivedExceptionsMap.get(mLastType) == null) {
                exceptionStack = new BlockingStack<>();
                mReceivedExceptionsMap.put(mLastType, exceptionStack);
            } else {
                exceptionStack = mReceivedExceptionsMap.get(mLastType);
            }
            exceptionStack.put(exception);
        } catch (InterruptedException e) {
            //ignored
        }
        return this;
    }

    /**
     * Constructs dialogs
     */
    public void showDialog(final Context context, Callback callback) {
        DialogUtils.Builder dialog = new DialogUtils.Builder(context)
                .setTitle(mLastType.getTitle())
                .setMessage(mLastType.getMessage())
                .setNeutralButton(mLastType.getNeutralText(), getNeutralListener(context))
                .setNegativeButton();

        if (mLastProcessedException instanceof HttpException) {
            dialog.setPositiveButton(mLastType.getPositiveText(), ((HttpException) mLastProcessedException).getRequest(), callback);
        }

        dialog.createAndShow();
        mReceivedExceptionsMap.remove(mLastType);
    }

    private DialogInterface.OnClickListener getNeutralListener(final Context context) {
        DialogInterface.OnClickListener neutralListener = null;
        if (mLastType == ExceptionType.NO_INTERNET) {
            neutralListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                }
            };
        }
        return neutralListener;
    }

}
