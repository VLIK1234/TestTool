package amtt.epam.com.amtt.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by Ivan_Bakach on 02.06.2015.
 */
public class GlobalBroadcastReciever extends BroadcastReceiver {

    public static final String SHOW = "SHOW";
    public static final String LOG_FILE = "LOG_FILE";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case LOG_FILE:
                Bundle extras = intent.getExtras();
                String fielPath="";
                if (extras!=null) {
                    fielPath = extras.getString("filePath");
                }
                intent.getExtras();
                Toast.makeText(context, fielPath, Toast.LENGTH_LONG).show();break;

        }
    }

    public static void registerBroadcastReciver(Context context, GlobalBroadcastReciever broadcastReceiver){
        IntentFilter intentFilter = new IntentFilter();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void unregisterBroadcastReciver(Context context, GlobalBroadcastReciever broadcastReceiver){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }
}
