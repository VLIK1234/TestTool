package amtt.epam.com.amtt.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
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
        Preference removeArtifacts = findPreference(getActivity().getString(R.string.key_remove_artifacts));
        removeArtifacts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), getColorString(getString(R.string.message_question_delete_artifacts)), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.WHITE)
                        .setAction(R.string.label_sure_delete_artifacts, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FileUtil.cleanAllUsersArtifacts();
                                Snackbar.make(getActivity().findViewById(android.R.id.content), getColorString(getString(R.string.message_done_delete_artifacts)), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                            }
                        })
                        .show();
                return true;
            }
        });
        Preference goToTesting = findPreference(getActivity().getString(R.string.key_go_to_testing));
        goToTesting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().finish();
                return true;
            }
        });

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

    private SpannableString getColorString(String value) {
        SpannableString spannableValue = new SpannableString(value);
        spannableValue.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableValue;
    }
}
