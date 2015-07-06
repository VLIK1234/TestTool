package amtt.epam.com.amtt.ui.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import amtt.epam.com.amtt.R;

/**
 @author Ivan_Bakach
 @version on 05.06.2015
 */

public class SettingsFragment extends PreferenceFragment {

    public static CheckBoxPreference checkBoxPreference;
    public static SwitchPreference switchPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        EditTextPreference projectName = (EditTextPreference) findPreference(getActivity().getString(R.string.key_test_project));
        projectName.setSummary(projectName.getText());
        checkBoxPreference = (CheckBoxPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_dialog_hide));
        switchPreference = (SwitchPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_topbutton_show));
    }
}
