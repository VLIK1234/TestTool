package amtt.epam.com.amtt.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Iryna Monchanka
 * @version on 03.06.2015
 */

public class AskExitActivity  extends Activity {

    boolean mIsDismiss = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);
        builder.setTitle(R.string.dialog_exit)
                .setNegativeButton(getResources().getString(R.string.label_return), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                })
                .setPositiveButton(getResources().getString(R.string.label_close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TopButtonService.close(getBaseContext());
                        mIsDismiss = false;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        if (mIsDismiss) {
                            TopButtonService.sendActionChangeTopButtonVisibility(true);
                        }
                    }
                })
                .show();
    }

}
