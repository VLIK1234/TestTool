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

    public static final String API_SDK = "API SDK=";
    public static final String DPI = "dpi";

    public static String TEMPLATE = "\n%s: %s";

    public static String getAppInfo(){
        String appInfo = "";

        try {
            final PackageInfo packageInfo =  ContextHolder.getContext().getPackageManager().getPackageInfo(ContextHolder.getContext().getPackageName(), 0);
            appInfo += String.format(TEMPLATE, "Version app", packageInfo.versionName);
            appInfo += String.format(TEMPLATE, "Name", ContextHolder.getContext().getResources().getString(packageInfo.applicationInfo.labelRes));
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    public static String getDeviceOsInfo(){
        String deviceInfo = "---Device info---"
                + String.format(TEMPLATE, "OS", getSystemVersionName())
                + String.format(TEMPLATE, "Device", Build.BRAND.toUpperCase() +" "+ Build.MODEL.toUpperCase())
                + String.format(TEMPLATE, "Baseband version", Build.getRadioVersion())
                + String.format(TEMPLATE, "Display", getInfoSizeDisplay());

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

        StringBuilder infoDensity = new StringBuilder();

        Field[] fields = DisplayMetrics.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == metrics.densityDpi&&!fieldName.equals("DENSITY_DEVICE")) {
                infoDensity.append(" ").append(fieldName).append(", ");
                infoDensity.append(fieldValue).append(DPI);
            }
        }
        return size.x+ " x "+size.y + infoDensity.toString();
    }

    public static String getSystemVersionName() {
        StringBuilder versionName = new StringBuilder();
        versionName.append("Android ").append(Build.VERSION.RELEASE);

        int fieldValue = -1;
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                versionName.append(" ").append(fieldName).append(", ");
            }
        }
        versionName.append(API_SDK).append(fieldValue);
        return versionName.toString();
    }


    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ContextHolder.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
