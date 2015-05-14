package amtt.epam.com.amtt.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.helper.StringFormatHelper;
import amtt.epam.com.amtt.helper.SystemInfoHelper;

/**
 * Created by Ivan_Bakach on 14.05.2015.
 */
public class InfoActivity extends BaseActivity {
    public static String ACTIVITY_NAME = "Activity name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView infoTextView = (TextView) findViewById(R.id.info);
        infoTextView.setText("---Activity info---");
        infoTextView.append(getReceivedInfoAboutActivity());
        infoTextView.append(StringFormatHelper.format("Internet status", SystemInfoHelper.getIntenetStatus()));
    }

    public String getReceivedInfoAboutActivity() {
        Bundle extras = getIntent().getExtras();
        String out = "";
        if (extras != null) {
            out += (StringFormatHelper.format(ACTIVITY_NAME, extras.getString(ACTIVITY_NAME)));
        }
        return out;
    }


}
