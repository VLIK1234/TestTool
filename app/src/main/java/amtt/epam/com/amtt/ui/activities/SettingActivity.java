package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.fragments.SettingsFragment;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.PreferenceUtil;
import amtt.epam.com.amtt.util.TestUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.06.2015
 */

public class SettingActivity extends AppCompatActivity {

    private Button mBtLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.fl_settings, new SettingsFragment()).commit();

//        Button btRemoveArtifacts = (Button) findViewById(R.id.bt_remove_artifacts);
//        btRemoveArtifacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(findViewById(android.R.id.content), getColorString(getString(R.string.message_question_delete_artifacts)), Snackbar.LENGTH_LONG)
//                        .setActionTextColor(Color.RED)
//                        .setAction(R.string.label_sure_delete_artifacts, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                FileUtil.cleanAllUsersArtifacts();
//                                Snackbar.make(findViewById(android.R.id.content), getColorString(getString(R.string.message_done_delete_artifacts)), Snackbar.LENGTH_LONG)
//                                        .setActionTextColor(Color.WHITE)
//                                        .show();
//                            }
//                        })
//                        .show();
//            }
//        });

        Button btLetsTest = (Button) findViewById(R.id.bt_go_test);
        btLetsTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testAppName = PreferenceUtil.getString(getString(R.string.key_test_project));
                if (!TextUtils.isEmpty(testAppName)) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(testAppName);
                    startActivity(launchIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), R.string.message_empty_test_app, Toast.LENGTH_LONG).show();
                }
            }
        });
        mBtLogOut = (Button) findViewById(R.id.bt_log_out);
        mBtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActiveUser.getInstance().getUserName() != null) {
                    finish();
                    Intent intentUserInfo = new Intent(SettingActivity.this, AskExitActivity.class);
                    startActivity(intentUserInfo);
                } else {
                    Intent intentLogin = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActiveUser.getInstance().getUserName() == null) {
            mBtLogOut.setText(R.string.label_login_button);
        } else {
            mBtLogOut.setText(R.string.label_logout_button);
        }
    }
}
