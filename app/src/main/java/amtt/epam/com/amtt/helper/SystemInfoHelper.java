package amtt.epam.com.amtt.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Ivan_Bakach on 14.05.2015.
 */
public class SystemInfoHelper {

    public static final int LOLIPOP_MR1 = 22;

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
                + StringHelper.format("System info", getNameVersionSystem() + ", API version - "+Build.VERSION.SDK_INT)
                + StringHelper.format("Device", Build.BRAND.toUpperCase() +" "+ Build.MODEL.toUpperCase())
                + StringHelper.format("Baseband version", Build.getRadioVersion())
                + StringHelper.format("Display", getInfoSizeDisplay());

        return deviceInfo;

    }

    public static String getIntenetStatus(){
        return isOnline()? "Connection active": "Connection non active";
    }

    public static String getInfoSizeDisplay(){
        WindowManager wm = (WindowManager) ContextHolder.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DisplayMetrics metrics = ContextHolder.getContext().getResources().getDisplayMetrics();

        String dpiInfo = "";
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                dpiInfo = " ldpi, "+ DisplayMetrics.DENSITY_LOW +"dpi";break;
            case DisplayMetrics.DENSITY_MEDIUM:
                dpiInfo = " mdpi, "+ DisplayMetrics.DENSITY_MEDIUM +"dpi";break;
            case DisplayMetrics.DENSITY_HIGH:
                dpiInfo = " hdpi, "+ DisplayMetrics.DENSITY_HIGH +"dpi";break;
            case DisplayMetrics.DENSITY_XHIGH:
                dpiInfo = " xhdpi, "+ DisplayMetrics.DENSITY_XHIGH +"dpi";break;
            case DisplayMetrics.DENSITY_400:
                dpiInfo = " xhdpi, "+ DisplayMetrics.DENSITY_400 +"dpi";break;
            case DisplayMetrics.DENSITY_XXHIGH:
                dpiInfo = " xxhdpi, "+ DisplayMetrics.DENSITY_XXHIGH +"dpi";break;
            case DisplayMetrics.DENSITY_560:
                dpiInfo = " xxxhdpi, "+ DisplayMetrics.DENSITY_560 +"dpi";break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                dpiInfo = " xxxhdpi, "+ DisplayMetrics.DENSITY_XXXHIGH +"dpi";break;
        }

        return size.x+ " x "+size.y + dpiInfo;
    }

    public static String getNameVersionSystem() {
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.FROYO:
                return "Android 2.2 Froyo";
            case Build.VERSION_CODES.GINGERBREAD:
                return "Android 2.3 Gingerbread";
            case Build.VERSION_CODES.GINGERBREAD_MR1:
                return "Android 2.3.3 Gingerbread";
            case Build.VERSION_CODES.HONEYCOMB:
                return "Android 3.0 Honeycomb";
            case Build.VERSION_CODES.HONEYCOMB_MR1:
                return "Android 3.1 Honeycomb";
            case Build.VERSION_CODES.HONEYCOMB_MR2:
                return "Android 3.2 Honeycomb";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                return "Android 4.0 Ice cream sandwich";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                return "Android 4.0.3 Ice cream sandwich";
            case Build.VERSION_CODES.JELLY_BEAN:
                return "Android 4.1 Jelly bean";
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
                return "Android 4.2 Jelly bean";
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
                return "Android 4.3 Jelly bean";
            case Build.VERSION_CODES.KITKAT:
                return "Android 4.4 Kitkat";
            case Build.VERSION_CODES.KITKAT_WATCH:
                return "Android 4.4W Kitkat for watches";
            case Build.VERSION_CODES.LOLLIPOP:
                return "Android 5.0 Lolipop";
            case LOLIPOP_MR1:
                return "Android 5.1 Lolipop";
            default:
                return "Android "+Build.VERSION_CODES.CUR_DEVELOPMENT+".0" + " A long time ago in a galaxy far, far away....";
        }
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ContextHolder.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
