package amtt.epam.com.amtt.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.SettingsFragment;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 @author Ivan_Bakach
 @version on 05.06.2015
 */

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_dialog_hide))) {
        }else if (key.equals(getString(R.string.key_topbutton_show))) {
            TopButtonService.sendActionChangeTopButtonVisibility(sharedPreferences.getBoolean(getString(R.string.key_topbutton_show), true));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(this);
        finish();
    }
}
