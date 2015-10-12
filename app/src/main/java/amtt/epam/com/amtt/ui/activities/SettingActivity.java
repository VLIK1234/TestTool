package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.SettingsFragment;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.06.2015
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.fl_settings, new SettingsFragment()).commit();

        Button btRemoveArtifacts = (Button) findViewById(R.id.bt_remove_artifacts);
        btRemoveArtifacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), getColorString(getString(R.string.message_question_delete_artifacts)), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .setAction(R.string.label_sure_delete_artifacts, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FileUtil.cleanAllUsersArtifacts();
                                Snackbar.make(findViewById(android.R.id.content), getColorString(getString(R.string.message_done_delete_artifacts)), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        Button btLetsTest = (Button) findViewById(R.id.bt_go_test);
        btLetsTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btLogOut = (Button) findViewById(R.id.bt_log_out);
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUserInfo = new Intent(SettingActivity.this, UserInfoActivity.class);
                startActivity(intentUserInfo);
            }
        });
    }

    private SpannableString getColorString(String value) {
        SpannableString spannableValue = new SpannableString(value);
        spannableValue.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableValue;
    }
}
