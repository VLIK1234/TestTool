package amtt.epam.com.amtt.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.activities.SettingActivity;

/**
 @author Ivan_Bakach
 @version on 06.07.2015
 */

public class TestUtil {

    private static final String CLOSE_TEST = "CLOSE_TEST";
    private static final String TAG = "TAG";

    private static InstrumentationInfo getInstrumentationInfo(final String packageName) {
        final List<InstrumentationInfo> list =
                CoreApplication.getContext().getPackageManager()
                        .queryInstrumentation(packageName, 0);
        return (!list.isEmpty()) ? list.get(0) : null;
    }

    public static String[][] getTestedApps() {
        ArrayList<PackageInfo> res = new ArrayList<>();
        List<PackageInfo> packs = CoreApplication.getContext().getPackageManager().getInstalledPackages(PackageManager.GET_INSTRUMENTATION);
        for (PackageInfo packageInfo : packs) {
            try {
                InstrumentationInfo info = TestUtil.getInstrumentationInfo(packageInfo.packageName);
                if (info != null) {
                    ComponentName componentName = new ComponentName(info.packageName, info.name);
                    InstrumentationInfo instrumentationInfo = CoreApplication.getContext().getPackageManager().getInstrumentationInfo(componentName, PackageManager.GET_META_DATA);
                    Logger.d(TAG, instrumentationInfo.name + " " + 1);
                    if (!res.contains(packageInfo.packageName)) {
                        res.add(packageInfo);
                        Logger.d(TAG, packageInfo.packageName + " " + 2);
                    }
                    /*Not used in real life
                    * P.S. On real device.*/
//                    Bundle bundle = instrumentationInfo.metaData;
//                    if (bundle != null) {
//                        Log.d(TAG, instrumentationInfo.name + " ");
//                        Log.d(TAG, "BUNDLE "+bundle.toString());
//                        String myApiKey = bundle.getString(AMTT_TEST_KEY);
//                        if (AMTT_APP_VALUE.equals(myApiKey)) {
//                            res.insert(instrumentationInfo.targetPackage);
//                        }
//                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Logger.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            } catch (NullPointerException e) {
                Logger.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            } catch (ClassCastException e){
                Logger.e(TAG, "Failed to load meta-data, ClassCastException: " + e.getMessage());
            }
        }
        String[] arrayEntries = new String[res.size()];
        String[] arrayEntriesValues = new String[res.size()];
        for (int i=0; i<res.size();i++) {
            arrayEntries[i]=res.get(i).applicationInfo.loadLabel(CoreApplication.getContext().getPackageManager()).toString();
            arrayEntriesValues[i]=res.get(i).packageName;
        }
        String[][] resultArrays = new String[2][];
        resultArrays[0]=arrayEntries;
        resultArrays[1]=arrayEntriesValues;
        return resultArrays;
    }

    private static void runTests() {
        final String pn = PreferenceUtil.getString(CoreApplication.getContext().getString(R.string.key_test_project));
        final InstrumentationInfo info = getInstrumentationInfo(pn);
        if (info != null) {
            final ComponentName cn = new ComponentName(info.packageName,
                    info.name);
            try{
                CoreApplication.getContext().startInstrumentation(cn, null, null);
            }catch (SecurityException e){
                Toast.makeText(CoreApplication.getContext(), R.string.error_message_signature_run_test, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CoreApplication.getContext(),
                    "Cannot find instrumentation for " + pn, Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(CoreApplication.getContext(), SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CoreApplication.getContext().startActivity(intent);
            Toast.makeText(CoreApplication.getContext(), "Please choose tested project",Toast.LENGTH_LONG).show();
        }
    }

    public static void closeTest() {
        Intent in = new Intent();
        in.setAction(CLOSE_TEST);
        CoreApplication.getContext().sendBroadcast(in);
    }

    public static void restartTest(){
        closeTest();
        runTests();
    }

}
