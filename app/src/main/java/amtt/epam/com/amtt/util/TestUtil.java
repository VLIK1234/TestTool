package amtt.epam.com.amtt.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 06.07.2015.
 */
public class TestUtil {

    public static final String CLOSE_TEST = "CLOSE_TEST";
    public static final String AMTT_TEST_KEY = "amtt_test_key";
    public static final String AMTT_APP_VALUE = "amtt_app";
    public static final String TAG = "TAG";

    public static InstrumentationInfo getInstrumentationInfo(final String packageName) {
        final List<InstrumentationInfo> list =
                AmttApplication.getContext().getPackageManager()
                        .queryInstrumentation(packageName, 0);
        return (!list.isEmpty()) ? list.get(0) : null;
    }

    public static String[] getTestedApps() {
        ArrayList<String> res = new ArrayList<>();
        List<PackageInfo> packs = AmttApplication.getContext().getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packs) {
            try {
                InstrumentationInfo info = TestUtil.getInstrumentationInfo(packageInfo.packageName);
                if (info != null) {
                    ComponentName componentName = new ComponentName(info.packageName, info.name);
                    InstrumentationInfo instrumentationInfo = AmttApplication.getContext().getPackageManager().getInstrumentationInfo(componentName, PackageManager.GET_META_DATA);
                    Log.d(TAG, instrumentationInfo.name + " ");
                    if (instrumentationInfo != null) {
                        Log.d(TAG, instrumentationInfo + " ");
                    }
                    Bundle bundle = instrumentationInfo.metaData;
                    if (bundle != null) {
                        Log.d(TAG, instrumentationInfo.name + " ");
                        String myApiKey = bundle.getString(AMTT_TEST_KEY);
                        if (AMTT_APP_VALUE.equals(myApiKey)) {
                            res.add(instrumentationInfo.targetPackage);
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }
        return res.toArray(new String[res.size()]);
    }

    public static void runTests() {
        final String pn = PreferenceUtil.getString(AmttApplication.getContext().getString(R.string.key_test_project));
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

    public static void closeTest() {
        Intent in = new Intent();
        in.setAction(CLOSE_TEST);
        AmttApplication.getContext().sendBroadcast(in);
    }

}
