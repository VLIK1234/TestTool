package amtt.epam.com.amtt.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 06.07.2015.
 */
public class TestUtil {

    public static final String CLOSE_TEST = "CLOSE_TEST";

    private static InstrumentationInfo getInstrumentationInfo(final String packageName) {
        final List<InstrumentationInfo> list =
                AmttApplication.getContext().getPackageManager()
                        .queryInstrumentation(packageName, 0);
        for (InstrumentationInfo info:list) {
            Log.e("TAG", info.name);
        }
        return (!list.isEmpty()) ? list.get(0) : null;
    }

    public static void runTests() {
        final String pn = PreferenceUtils.getString(AmttApplication.getContext().getString(R.string.key_test_project));
        final InstrumentationInfo info = getInstrumentationInfo(pn);
        if (info != null) {
            final ComponentName cn = new ComponentName(info.packageName,
                    info.name);
            AmttApplication.getContext().startInstrumentation(cn, null, null);
        } else {
            Toast.makeText(AmttApplication.getContext(),
                    "Cannot find instrumentation for " + pn, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static void closeTest(){
        Intent in = new Intent();
        in.setAction(CLOSE_TEST);
        AmttApplication.getContext().sendBroadcast(in);
        Toast.makeText(AmttApplication.getContext(), in.getAction(), Toast.LENGTH_LONG).show();
    }

}
