package amtt.epam.com.amtt.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * Created by Ivan_Bakach on 05.06.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    public static CheckBoxPreference checkBoxPreference;
    public static SwitchPreference switchPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        checkBoxPreference = (CheckBoxPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_dialog_hide));
        switchPreference = (SwitchPreference) findPreference(getActivity().getBaseContext().getString(R.string.key_topbutton_show));
    }
}
