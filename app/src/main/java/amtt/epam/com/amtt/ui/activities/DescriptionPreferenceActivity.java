package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 * @author IvanBakach
 * @version on 15.10.2015
 */
public class DescriptionPreferenceActivity extends BaseActivity{

    private EditText mDescriptionTemplate;
    private String mKeyDescriptionTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_preference);
        overridePendingTransition(R.anim.anim_entre_activity, R.anim.anim_exit_activity);
        mKeyDescriptionTemplate = getString(R.string.key_description_template);

        mDescriptionTemplate = (EditText) findViewById(R.id.et_descreption_template);
        mDescriptionTemplate.setText(PreferenceUtil.getString(mKeyDescriptionTemplate));

        Button okSaveBatton = (Button) findViewById(R.id.bt_ok_save_description);
        okSaveBatton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.putString(mKeyDescriptionTemplate, mDescriptionTemplate.getText().toString());
                Intent intentSettingActivity = new Intent(DescriptionPreferenceActivity.this, SettingActivity.class);
                startActivity(intentSettingActivity);
            }
        });
        Button cancelSave = (Button) findViewById(R.id.bt_cancel_save_description);
        cancelSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettingActivity = new Intent(DescriptionPreferenceActivity.this, SettingActivity.class);
                startActivity(intentSettingActivity);
            }
        });
    }
}
