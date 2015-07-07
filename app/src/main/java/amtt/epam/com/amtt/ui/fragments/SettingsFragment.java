package amtt.epam.com.amtt.ui.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.TestUtil;

/**
 @author Ivan_Bakach
 @version on 05.06.2015
 */

public class SettingsFragment extends PreferenceFragment {

    public static CheckBoxPreference checkBoxPreference;
    public static SwitchPreference switchPreference;
    public ListPreference projectName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        projectName = (ListPreference) findPreference(getActivity().getString(R.string.key_test_project));
        initListValue();
        projectName.setSummary(projectName.getValue());
        checkBoxPreference = (CheckBoxPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_dialog_hide));
        switchPreference = (SwitchPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_topbutton_show));
    }

    public void initListValue(){
        String[] array = TestUtil.getTestedApps(false);;
        projectName.setEntries(array);
        projectName.setEntryValues(array);
    }


}
