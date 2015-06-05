package amtt.epam.com.amtt.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 05.06.2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        CheckBoxPreference  checkBoxPreference = (CheckBoxPreference) findPreference("dialog_show");
        checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity().getBaseContext(),  preference.isSelectable()+ " is cheked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(getActivity().getBaseContext(),  preference.isSelectable()+ " is cheked", Toast.LENGTH_SHORT).show();
                return preference.isEnabled();
            }
        });
    }
}
