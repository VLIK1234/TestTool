package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import amtt.epam.com.amtt.ui.fragments.SettingsFragment;

/**
 * @author Ivan_Bakach
 * @version on 05.06.2015
 */

public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
