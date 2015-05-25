package amtt.epam.com.amtt.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Ivan_Bakach on 14.05.2015.
 */
public class SystemInfoHelper {

    public static String getAppInfo(){
        String appInfo = "";

        try {
            final PackageInfo packageInfo =  ContextHolder.getContext().getPackageManager().getPackageInfo(ContextHolder.getContext().getPackageName(), 0);
            appInfo += StringHelper.format("Version app", packageInfo.versionName);
            appInfo += StringHelper.format("Name", ContextHolder.getContext().getResources().getString(packageInfo.applicationInfo.labelRes));
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    public static String getDeviceOsInfo(){
        String deviceInfo = "---Device info---"
                + StringHelper.format("Version", Build.VERSION.SDK_INT)
                + StringHelper.format("Board", Build.BOARD)
                + StringHelper.format("Brand", Build.BRAND)
                + getCompatAbi()
                + StringHelper.format("Display", Build.DISPLAY)
                + StringHelper.format("Device", Build.DEVICE)
                + StringHelper.format("Fingerprint", Build.FINGERPRINT)
                + StringHelper.format("Id", Build.ID)
                + StringHelper.format("Manufacturer", Build.MANUFACTURER)
                + StringHelper.format("Model", Build.MODEL)
                + StringHelper.format("Product", Build.PRODUCT);
        String systemProperties = "\n\n---System properties---"
                + StringHelper.format("Bootloader", Build.BOOTLOADER)
                + StringHelper.format("Hardware", Build.HARDWARE)
                + StringHelper.format("Radio firmware", Build.getRadioVersion())
                + StringHelper.format("Serial number", Build.SERIAL)
                + StringHelper.format("Build type", Build.TYPE);

        return deviceInfo + systemProperties;

    }

    public static String getCompatAbi() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
            return StringHelper.format("Cpu Abi", Build.CPU_ABI)+ StringHelper.format("Cpu Abi2", Build.CPU_ABI2);
        }else{
            return StringHelper.format("Supported abis", Build.SUPPORTED_ABIS)
                    + StringHelper.format("Supported 32 bit abis", Build.SUPPORTED_32_BIT_ABIS)
                    + StringHelper.format("Supported 64 bit abis", Build.SUPPORTED_64_BIT_ABIS);
        }
    }

    public static String getIntenetStatus(){
        return isOnline()? "Connection active": "Connection non active";
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ContextHolder.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
