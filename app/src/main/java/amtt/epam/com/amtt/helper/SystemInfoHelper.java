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
            appInfo += StringFormatHelper.format("Version app", packageInfo.versionName);
            appInfo += StringFormatHelper.format("Name", ContextHolder.getContext().getResources().getString(packageInfo.applicationInfo.labelRes));
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    public static String getDeviceOsInfo(){
        String deviceInfo = "\n\n---Device info---"
                + StringFormatHelper.format("Version", Build.VERSION.SDK_INT)
                + StringFormatHelper.format("Board", Build.BOARD)
                + StringFormatHelper.format("Brand", Build.BRAND)
                + getCompatAbi()
                + StringFormatHelper.format("Display", Build.DISPLAY)
                + StringFormatHelper.format("Device", Build.DEVICE)
                + StringFormatHelper.format("Fingerprint", Build.FINGERPRINT)
                + StringFormatHelper.format("Id", Build.ID)
                + StringFormatHelper.format("Manufacturer", Build.MANUFACTURER)
                + StringFormatHelper.format("Model", Build.MODEL)
                + StringFormatHelper.format("Product", Build.PRODUCT);
        String systemProperties = "\n\n---System properties---"
                + StringFormatHelper.format("Bootloader", Build.BOOTLOADER)
                + StringFormatHelper.format("Hardware", Build.HARDWARE)
                + StringFormatHelper.format("Radio firmware", Build.getRadioVersion())
                + StringFormatHelper.format("Serial number", Build.SERIAL)
                + StringFormatHelper.format("Build type", Build.TYPE);

        return deviceInfo + systemProperties;

    }

    public static String getCompatAbi() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
            return StringFormatHelper.format("Cpu Abi", Build.CPU_ABI)+StringFormatHelper.format("Cpu Abi2", Build.CPU_ABI2);
        }else{
            return StringFormatHelper.format("Supported abis", Build.SUPPORTED_ABIS)
                    +StringFormatHelper.format("Supported 32 bit abis", Build.SUPPORTED_32_BIT_ABIS)
                    +StringFormatHelper.format("Supported 64 bit abis", Build.SUPPORTED_64_BIT_ABIS);
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
