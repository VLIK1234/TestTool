package amtt.epam.com.amtt.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class BaseActivity extends AppCompatActivity {

    static final int CURSOR_LOADER_ID = 0;
    static final int NO_FLAGS = 0;
    public final static String ACTION_SAVE_STEP = "amtt.epam.com.amtt.app.SAVESTEP";
    private IntentFilter intentFilterBroadcast = new IntentFilter(ACTION_SAVE_STEP);
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SAVE_STEP)) {
                //Realization save step
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        registerReceiver(broadcastReceiver, intentFilterBroadcast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void showProgress(boolean show) {
        View progressBar = findViewById(getProgressViewId());
        if (progressBar != null) {
            findViewById(R.id.progress).setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected int getProgressViewId() {
        return R.id.progress;
    }
}
