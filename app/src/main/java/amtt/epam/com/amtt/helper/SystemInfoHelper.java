package amtt.epam.com.amtt.helper;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.util.RootUtil;

/**
 * @author Ivan_Bakach
 * @version on 14.05.2015
 */

public class SystemInfoHelper {

    public static final String API_SDK = "API SDK=";
    public static final String DPI = "dpi";

    public static Spanned getDeviceOsInfo() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(Html.fromHtml("<h5>" + "Device info: " + "</h5>" +
                "<b>" + "OS: " + "</b>" + "<small>" + getSystemVersionName() + "</small>" + "<br />" +
                "<b>" + "Device: " + "</b>" + "<small>" + Build.BRAND.toUpperCase() + " " + Build.MODEL.toUpperCase() + "</small>" + "<br />" +
                "<b>" + "Display: " + "</b>" + "<small>" + getInfoSizeDisplay() + "</small>" + "<br />" +
                "<b>" + "Root: " + "</b>" + "<small>" + RootUtil.isDeviceRooted() + "</small>"));
        return builder;

    }

    public static String getIntenetStatus() {
        return isOnline() ? "Connection active" : "Connection non active";
    }

    public static String getInfoSizeDisplay() {
        WindowManager wm = (WindowManager) AmttApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DisplayMetrics metrics = AmttApplication.getContext().getResources().getDisplayMetrics();

        StringBuilder infoDensity = new StringBuilder();

        Field[] fields = DisplayMetrics.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                //ignore because reflection produce some exception during searched field value
            }

            if (fieldValue == metrics.densityDpi && !fieldName.equals("DENSITY_DEVICE")) {
                infoDensity.append(" ").append(fieldName).append(", ");
                infoDensity.append(fieldValue).append(DPI);
            }
        }
        return size.x + " x " + size.y + infoDensity.toString();
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
                (ConnectivityManager) AmttApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
