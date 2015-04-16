package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.StepSavingCallback;
import amtt.epam.com.amtt.database.StepSavingResult;
import amtt.epam.com.amtt.database.StepSavingTask;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class BaseActivity extends Activity{

    public static final String LOG_TAG = "TAG";
    public final static String ACTION_SAVE_STEP = "amtt.epam.com.amtt.app.TAKESCREENSHOT";

    protected BroadcastReceiver br;
    private IntentFilter intentFilterBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_SAVE_STEP)) {
                    //Realization save step
                }
            }
        };
        intentFilterBroadcast = new IntentFilter(ACTION_SAVE_STEP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        registerReceiver(br, intentFilterBroadcast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    public void showProgress(boolean show) {
        if (findViewById(getProgressViewId())!=null) {
            findViewById(getProgressViewId()).setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected int getProgressViewId() {
        return R.id.progress;
    }
}
