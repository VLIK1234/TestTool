package amtt.epam.com.amtt.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.broadcastreceiver.GlobalBroadcastReceiver;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 * @author Ivan_Bakach
 * @version on 29.07.2015
 */
public class TakeStepActivity extends AppCompatActivity {

    private static final String EXTERNAL_ACTION_TAKE_SCREENSHOT = "TAKE_SCREENSHOT";
    private static final String TAKE_ONLY_INFO = "TAKE_ONLY_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        LinearLayout rootLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_take_screen, null);
        final CheckBox takeActivityInfo = (CheckBox) rootLayout.findViewById(R.id.cb_take_activity_info);
        final CheckBox takeScreen = (CheckBox) rootLayout.findViewById(R.id.cb_take_screen);
        takeActivityInfo.setChecked(PreferenceUtil.getBoolean(getString(R.string.checkbox_activity_info_key)));
        takeScreen.setChecked(PreferenceUtil.getBoolean(getString(R.string.checkbox_screen_key)));

        final AlertDialog takeStepDialog = new AlertDialog.Builder(this, R.style.Dialog)
                .setTitle(getString(R.string.take_step_dialog_title))
                .setMessage(getString(R.string.take_step_dialog_message))
                .setView(rootLayout)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.take_step_dialog_positive_button), null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                }).create();
        takeStepDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button takeButton = takeStepDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                takeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!takeActivityInfo.isChecked() && takeScreen.isChecked()) {
                            Intent intent = new Intent();
                            intent.setAction(EXTERNAL_ACTION_TAKE_SCREENSHOT);
                            sendBroadcast(intent);
                            TopButtonService.sendActionChangeTopButtonVisibility(false);
                            GlobalBroadcastReceiver.setStepWithoutActivityInfo(true);
                            saveCheckboxValue(takeActivityInfo.isChecked(), takeScreen.isChecked());
                            takeStepDialog.dismiss();
                        } else if (takeActivityInfo.isChecked() && !takeScreen.isChecked()) {
                            Intent intent = new Intent();
                            intent.setAction(TAKE_ONLY_INFO);
                            sendBroadcast(intent);
                            saveCheckboxValue(takeActivityInfo.isChecked(), takeScreen.isChecked());
                            takeStepDialog.dismiss();
                        } else if (takeActivityInfo.isChecked() && takeScreen.isChecked()) {
                            Intent intent = new Intent();
                            intent.setAction(EXTERNAL_ACTION_TAKE_SCREENSHOT);
                            sendBroadcast(intent);
                            saveCheckboxValue(takeActivityInfo.isChecked(), takeScreen.isChecked());
                            takeStepDialog.dismiss();
                        } else {
                            Toast.makeText(getBaseContext(), getString(R.string.take_step_dialog_error_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        takeStepDialog.show();
    }

    private void saveCheckboxValue(boolean activityInfoIsChecked, boolean screenIsChecked) {
        PreferenceUtil.putBoolean(getString(R.string.checkbox_activity_info_key), activityInfoIsChecked);
        PreferenceUtil.putBoolean(getString(R.string.checkbox_screen_key), screenIsChecked);
    }
}
