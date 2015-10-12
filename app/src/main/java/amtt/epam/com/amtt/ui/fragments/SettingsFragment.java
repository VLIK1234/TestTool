package amtt.epam.com.amtt.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.topbutton.view.TopButtonView;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.PreferenceUtil;
import amtt.epam.com.amtt.util.TestUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.06.2015
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference projectName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        projectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(this);
        SwitchPreference widgetPreff = (SwitchPreference) findPreference(getResources().getString(R.string.key_topbutton_show));
        widgetPreff.setChecked(TopButtonService.getStateVisibilityButton());
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
            PreferenceUtil.putString(getString(R.string.key_test_project_entry), projectName.getEntry().toString());
            TestUtil.restartTest();
            projectName.setSummary(projectName.getEntry());
        }
    }
}
