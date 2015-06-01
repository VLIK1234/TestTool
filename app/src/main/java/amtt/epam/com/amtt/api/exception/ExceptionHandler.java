package amtt.epam.com.amtt.api.exception;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.JiraCallback;
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

    private static Map<ExceptionType, BlockingStack<AmttException>> mReceivedExceptionsMap;

    private AmttException mLastProcessedException;
    private ExceptionType mLastType;
    private boolean isDialogShown;

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
    public ExceptionHandler processError(final AmttException amttException) {
        mLastProcessedException = amttException;
        mLastType = ExceptionType.valueOf(amttException);
        isDialogShown = mReceivedExceptionsMap.get(mLastType) != null;

        try {
            BlockingStack<AmttException> exceptionStack;
            if (mReceivedExceptionsMap.get(mLastType) == null) {
                exceptionStack = new BlockingStack<>();
                mReceivedExceptionsMap.put(mLastType, exceptionStack);
            } else {
                exceptionStack = mReceivedExceptionsMap.get(mLastType);
            }
            exceptionStack.put(amttException);
        } catch (InterruptedException e) {
            //ignored
        }
        return this;
    }

    /**
     * Constructs dialogs
     */
    public void showDialog(final Context context, JiraCallback jiraCallback) {
        if (!isDialogShown) {
            new DialogUtils.Builder(context)
                    .setTitle(mLastType.getTitle())
                    .setMessage(mLastType.getMessage())
                    .setPositiveButton(mLastType.getPositiveText(), mLastProcessedException.getRestMethod(), jiraCallback)
                    .setNeutralButton(mLastType.getNeutralText(), getNeutralListener(context))
                    .setNegativeButton()
                    .createAndShow();
            mReceivedExceptionsMap.remove(mLastType);
        }
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
