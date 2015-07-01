package amtt.epam.com.amtt.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import amtt.epam.com.amtt.observer.AmttFileObserver;

/**
 @author Ivan_Bakach
 @version on 02.06.2015
 */

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String SHOW = "SHOW";
    public static final String LOG_FILE = "LOG_FILE";
    public static String logFilePath = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case LOG_FILE:
                Bundle extras = intent.getExtras();
                String filePath;
                if (extras!=null) {
                    filePath = extras.getString("filePath");
                    logFilePath = filePath;
                    if (!AmttFileObserver.getImageArray().contains(logFilePath)) {
                        AmttFileObserver.addToImageArray(logFilePath);
                    }
                    Toast.makeText(context, "Create log in file "+logFilePath, Toast.LENGTH_LONG).show();break;
                }
        }
    }
}
