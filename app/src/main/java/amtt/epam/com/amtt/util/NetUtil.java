package amtt.epam.com.amtt.util;

import android.content.Context;
import android.net.ConnectivityManager;

import amtt.epam.com.amtt.AmttApplication;

/**
 * Created by Artsiom_Kaliaha on 24.06.2015.
 * Information about Internet connection state
 */
public final class NetUtil {

    private final static ConnectivityManager sConnectivityManager;

    static {
        sConnectivityManager = (ConnectivityManager) AmttApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isAnyConnection() {
        return sConnectivityManager.getActiveNetworkInfo() != null;
    }

}
