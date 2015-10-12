package amtt.epam.com.amtt.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.PreferenceUtil;
import amtt.epam.com.amtt.util.TestUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.06.2015
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference mProjectName;
    private ListPreference mProjectJiraName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mProjectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
        mProjectJiraName = (ListPreference) findPreference(getActivity().getString(R.string.key_jira_project_name));
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(this);
        SwitchPreference widgetPreff = (SwitchPreference) findPreference(getResources().getString(R.string.key_topbutton_show));
        widgetPreff.setChecked(TopButtonService.getStateVisibilityButton());
        EditTextPreference descriptionTemplate = (EditTextPreference) findPreference(getString(R.string.key_description));
        if (ActiveUser.getInstance().getUserName() == null) {
            mProjectJiraName.setEnabled(false);
            descriptionTemplate.setEnabled(false);
        } else {
            mProjectJiraName.setEnabled(true);
            descriptionTemplate.setEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initListAppToConnect(mProjectName);
        mProjectName.setSummary(mProjectName.getEntry());
        initListJiraProjects(mProjectJiraName);
        mProjectJiraName.setSummary(mProjectJiraName.getEntry());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initListAppToConnect(ListPreference listPreference) {
        String[][] testedApps = TestUtil.getTestedApps();
        try{
            listPreference.setEntries(testedApps[0]);
            listPreference.setEntryValues(testedApps[1]);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void initListJiraProjects(final ListPreference listPreference) {
        if (ActiveUser.getInstance().getUserName() != null) {
            JiraContent.getInstance().getProjectsNames(ActiveUser.getInstance().getId(), new GetContentCallback<HashMap<JProjects, String>>() {
                @Override
                public void resultOfDataLoading(final HashMap<JProjects, String> result) {
                    if (result != null) {
                        ArrayList<String> projectJiraNames = new ArrayList<>();
                        projectJiraNames.addAll(result.values());
                        String[] projectStringArray = projectJiraNames.toArray(new String[projectJiraNames.size()]);
                        listPreference.setEntries(projectStringArray);
                        listPreference.setEntryValues(projectStringArray);
                    }
                }
            });
        }
//        else {
//            String[] emptyArray = new String[0];
//            listPreference.setEnabled(false);
//            listPreference.setEntryValues(emptyArray);
//            listPreference.setEntryValues(emptyArray);
//        }
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
        } else if (key.equals(getString(R.string.key_jira_project_name))) {
            if (ActiveUser.getInstance().getUserName() !=null) {
                JiraContent.getInstance().getProjectKeyByName(PreferenceUtil.getString(getString(R.string.key_jira_project_name)), new GetContentCallback<String>() {
                    @Override
                    public void resultOfDataLoading(final String result) {
                        if (result != null) {
                            ActiveUser.getInstance().setLastProjectKey(result);
                        }
                    }
                });
            }
        }
    }
}
