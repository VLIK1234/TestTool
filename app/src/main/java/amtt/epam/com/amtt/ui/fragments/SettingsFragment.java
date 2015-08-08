package amtt.epam.com.amtt.ui.fragments;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import java.util.ArrayList;

import amtt.epam.com.amtt.AmttApplication;
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
    private static SwitchPreference switchPreference;
    private ListPreference projectName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        projectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
        switchPreference = (SwitchPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_topbutton_show));
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        initListValue();
        projectName.setSummary(projectName.getEntry());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initListValue() {
        String[][] testedApps = TestUtil.getTestedApps();
        try{
            projectName.setEntries(testedApps[0]);
            projectName.setEntryValues(testedApps[1]);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_topbutton_show))) {
            TopButtonService.sendActionChangeTopButtonVisibility(sharedPreferences.getBoolean(getString(R.string.key_topbutton_show), true));
        } else if (key.equals(getString(R.string.key_test_project))) {
            ListPreference projectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
            projectName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return true;
                }
            });
            TestUtil.restartTest();
            projectName.setSummary(projectName.getEntry());
        }
    }
}
