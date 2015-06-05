package amtt.epam.com.amtt.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.fragment.SettingsFragment;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * Created by Ivan_Bakach on 05.06.2015.
 */
public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_dialog_hide))) {
            SettingsFragment.checkBoxPreference.setSelectable(sharedPreferences.getBoolean(key, false));
        }else if (key.equals(getString(R.string.key_topbutton_show))) {
            SettingsFragment.switchPreference.setSelectable(sharedPreferences.getBoolean(key, false));
            TopButtonService.sendActionChangeVisibilityButton();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
