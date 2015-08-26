package amtt.epam.com.amtt.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.exception.ExceptionType;

/**
 @author Artsiom_Kaliaha
 @version on 29.04.2015
 */

public class DialogUtils {


    public static AlertDialog createDialog(Context context, ExceptionType exceptionType) {
        return new AlertDialog.Builder(context)
                .setTitle(exceptionType.getTitle())
                .setMessage(exceptionType.getMessage())
                .setNegativeButton(R.string.error_button_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

}
