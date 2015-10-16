package amtt.epam.com.amtt.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.activities.DescriptionPreferenceActivity;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.FileUtil;
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

        Preference removeArtifacts = findPreference(getString(R.string.key_remove_artifacts));
        removeArtifacts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), getColorString(getString(R.string.message_question_delete_artifacts)), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
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
        Preference descriptionTemplate = findPreference(getString(R.string.key_description));
        descriptionTemplate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (ActiveUser.getInstance().getUserName() == null) {
                    Toast.makeText(getActivity(), R.string.message_anonym_deny_setting, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    Intent descriptionPrefActivity = new Intent(getActivity(), DescriptionPreferenceActivity.class);
                    startActivity(descriptionPrefActivity);
                    return true;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initListAppToConnect(mProjectName);
        mProjectName.setSummary(mProjectName.getEntry());
        initListJiraProjects(mProjectJiraName);
        if (ActiveUser.getInstance().getUserName()!=null) {
            mProjectJiraName.setSummary(PreferenceUtil.getString(getString(R.string.key_jira_project_name)));
        }
        showHintProjectJiraName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void showHintProjectJiraName() {
        if (ActiveUser.getInstance().getUserName() == null) {
            mProjectJiraName.setEntryValues(new String[0]);
            mProjectJiraName.setEntries(new String[0]);
        }
        mProjectJiraName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (ActiveUser.getInstance().getUserName() == null) {
                    mProjectJiraName.getDialog().dismiss();
                    Toast.makeText(getActivity(), R.string.message_anonym_deny_setting, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
        });
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
            mProjectJiraName.setSummary(mProjectJiraName.getEntry());
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

    private SpannableString getColorString(String value) {
        SpannableString spannableValue = new SpannableString(value);
        spannableValue.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableValue;
    }
}
