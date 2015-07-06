package amtt.epam.com.amtt.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.helper.HelpTakeScreen;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.PreferenceUtils;

/**
 @author Ivan_Bakach
 @version on 19.05.2015
 */

public class HelpDialogActivity extends Activity {

    public static final String IS_CAN_TAKE_SCREENSHOT = "IS_CAN_TAKE_SCREENSHOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsCanTakeScreenshot(true);
        LayoutInflater factory = LayoutInflater.from(getBaseContext());
        final View view = factory.inflate(R.layout.activity_dialog, null);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(getString(R.string.dialog_description))
                .append(Html.fromHtml(" <br /><b>" + getMessageForCurrentDevice() + "</b><br />"));

        CheckBox checkShowAgain = (CheckBox) view.findViewById(R.id.dialog_check_show_again);
        checkShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtils.putBoolean(getString(R.string.key_dialog_hide), isChecked);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);
        builder.setTitle(R.string.title_activity_help_dialog)
                .setMessage(stringBuilder)
                .setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setIsCanTakeScreenshot(false);
                        finish();
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    private String getMessageForCurrentDevice() {
        if (Build.MODEL.toUpperCase().contains(HelpTakeScreen.Constants.NEXUS.toUpperCase())) {
            return HelpTakeScreen.NEXUS.getValue();
        } else {
            switch (Build.BRAND.toUpperCase()){
                case HelpTakeScreen.Constants.SAMSUNG:
                    return HelpTakeScreen.SAMSUNG.getValue();
                case HelpTakeScreen.Constants.SONY:
                    return HelpTakeScreen.SONY.getValue();
                case HelpTakeScreen.Constants.HTC:
                    return HelpTakeScreen.HTC.getValue();
                case HelpTakeScreen.Constants.HUAWEI:
                    return HelpTakeScreen.HUAWEI.getValue();
                case HelpTakeScreen.Constants.XIAOMI:
                    return HelpTakeScreen.XIAOMI.getValue();
                default:
                    return HelpTakeScreen.ALL.getValue();
            }
        }
    }
    public static boolean getIsCanTakeScreenshot(){
        return PreferenceUtils.getBoolean(IS_CAN_TAKE_SCREENSHOT);
    }
    public static void setIsCanTakeScreenshot(boolean value){
        PreferenceUtils.putBoolean(IS_CAN_TAKE_SCREENSHOT, value);
    }
}
