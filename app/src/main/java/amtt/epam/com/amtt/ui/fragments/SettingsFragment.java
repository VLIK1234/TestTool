package amtt.epam.com.amtt.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.PreferenceUtil;
import amtt.epam.com.amtt.util.TestUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.06.2015
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static CheckBoxPreference checkBoxPreference;
    public static SwitchPreference switchPreference;
    public ListPreference projectName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        projectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
        initListValue();
        projectName.setSummary(projectName.getEntry());
        checkBoxPreference = (CheckBoxPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_dialog_hide));
        switchPreference = (SwitchPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_topbutton_show));
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void initListValue() {
        String[] array = TestUtil.getTestedApps();
        projectName.setEntries(array);
        projectName.setEntryValues(array);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_dialog_hide))) {
        } else if (key.equals(getString(R.string.key_topbutton_show))) {
            TopButtonService.sendActionChangeTopButtonVisibility(sharedPreferences.getBoolean(getString(R.string.key_topbutton_show), true));
        } else if (key.equals(getString(R.string.key_test_project))) {
            ListPreference projectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
            projectName.setSummary(projectName.getEntry());
        }
    }
}
