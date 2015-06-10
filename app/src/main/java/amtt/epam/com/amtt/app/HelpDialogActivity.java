package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.helper.HelpTakeScreen;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 @author Ivan_Bakach
 @version on 19.05.2015
 */

public class HelpDialogActivity extends Activity {

    private static boolean isCanTakeScreenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        setIsCanTakeScreenshot(false);

        TextView textView = (TextView) findViewById(R.id.message_dialog);
        textView.append(Build.BRAND.toUpperCase()+" "+Build.MODEL.toUpperCase());
        textView.append(getMessageForCurrentDevice());

        CheckBox checkShowAgain = (CheckBox) findViewById(R.id.dialog_check_show_again);
        checkShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                sharedPref.edit().putBoolean(getString(R.string.key_dialog_hide), isChecked).apply();
            }
        });
        Button buttonOk = (Button) findViewById(R.id.dialog_button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setIsCanTakeScreenshot(true);
            }
        });
        Button buttonCancel = (Button) findViewById(R.id.dialog_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopButtonService.sendActionChangeVisibilityButton();
                finish();
                setIsCanTakeScreenshot(false);
            }
        });
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
        return isCanTakeScreenshot;
    }
    public static void setIsCanTakeScreenshot(boolean value){
        isCanTakeScreenshot = value;
    }
}
