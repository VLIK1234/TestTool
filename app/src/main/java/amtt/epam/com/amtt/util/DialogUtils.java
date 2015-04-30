package amtt.epam.com.amtt.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;

/**
 * Created by Artsiom_Kaliaha on 29.04.2015.
 */
public class DialogUtils {

    /**
     * Builds dialogs for ExceptionHandler class
     */
    public static class Builder {

        private AlertDialog.Builder mBuilder;

        public Builder(Context context) {
            mBuilder = new AlertDialog.Builder(context);
        }

        public Builder setTitle(int titleId) {
            mBuilder.setTitle(titleId);
            return this;
        }

        public Builder setMessage(int messageId) {
            mBuilder.setMessage(messageId);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder setPositiveButton(int textId, final RestMethod restMethod, final JiraCallback callback) {
            if (textId != UtilConstants.Dialog.EMPTY_FIELD) {
                DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new JiraTask.Builder<>()
                                .setRestMethod(restMethod)
                                .setCallback(callback)
                                .createAndExecute();
                    }
                };
                mBuilder.setPositiveButton(textId, positiveListener);
            }
            return this;

        }

        public Builder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
            if (textId != UtilConstants.Dialog.EMPTY_FIELD && listener != null) {
                mBuilder.setNeutralButton(textId, listener);
            }
            return this;
        }

        public Builder setNegativeButton() {
            mBuilder.setNegativeButton(R.string.error_button_close, new DialogInterface.OnClickListener() {
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

}
