package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.helper.HelpTakeScreen;

/**
 * Created by Ivan_Bakach on 19.05.2015.
 */
public class HelpDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        
        TextView textView = (TextView) findViewById(R.id.message_dialog);
        textView.append(Build.BRAND.toUpperCase()+" "+Build.MODEL.toUpperCase());
        textView.append(getMessageForCurrentDevice());

        CheckBox checkShowAgain = (CheckBox) findViewById(R.id.dialog_check_show_again);
        checkShowAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button buttonOk = (Button) findViewById(R.id.dialog_button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                case HelpTakeScreen.Constants.HTC:
                    return HelpTakeScreen.HTC.getValue();
                case HelpTakeScreen.Constants.XIAOMI:
                    return HelpTakeScreen.XIAOMI.getValue();
                default:
                    return HelpTakeScreen.ALL.getValue();
            }
        }
    }
}
