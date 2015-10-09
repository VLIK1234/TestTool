package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.SettingsFragment;

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
    }
}
