package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.regex.Pattern;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.helper.StringHelper;

/**
 * Created by Ivan_Bakach on 19.05.2015.
 */
public class HelpDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        
        TextView textView = (TextView) findViewById(R.id.message_dialog);
        textView.append(Build.MANUFACTURER+" "+Build.MODEL);
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
    private String getMessageForCurrentDevice(){
        if (StringHelper.containsInsensetive(Build.MODEL, "Nexus")){
            return "\nNexus - volume down+power";
        }else if (StringHelper.containsInsensetive(Build.MANUFACTURER, "samsung")){
            return "\nSamsung - power+home";
        }else if (StringHelper.containsInsensetive(Build.MANUFACTURER, "HTC")) {
            return "\nHTC - power+home";
        }else if (StringHelper.containsInsensetive(Build.MANUFACTURER, "Xiaomi")) {
            return "\nXiaomi tablet - power+options \nXiaomi phone - volume down+power ";
        }else{
            return "\nSamsung - power+home \nNexus - volume down+power \nXiaomi tablet - power+options \nXiaomi phone - volume down+power \nHTC - power+home";
        }
    }
}
