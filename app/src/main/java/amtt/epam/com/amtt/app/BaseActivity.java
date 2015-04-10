package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.StepSavingCallback;
import amtt.epam.com.amtt.database.StepSavingResult;
import amtt.epam.com.amtt.database.StepSavingTask;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class BaseActivity extends Activity implements StepSavingCallback{

    public static final String LOG_TAG = "TAG";
    public final static String ACTION_TAKE_SCREENSHOT = "amtt.epam.com.amtt.app.TAKESCREENSHOT";

    protected BroadcastReceiver br;

    private int mScreenNumber = 1;
    private boolean newStepsSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_TAKE_SCREENSHOT)) {
                    newStepsSequence = true;
                    View rootView = getWindow().getDecorView();
                    rootView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = rootView.getDrawingCache();
                    Rect rect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    new StepSavingTask(BaseActivity.this, BaseActivity.this, bitmap, rect, BaseActivity.this.getComponentName(), newStepsSequence).execute();
                    newStepsSequence = false;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        //TODO why do we need to recreate receiver every time when onResume is called?

        IntentFilter intentFilterBroadcast = new IntentFilter(ACTION_TAKE_SCREENSHOT);
        registerReceiver(br, intentFilterBroadcast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    public void onStepSaved(StepSavingResult result) {
        int resultMessage = result == StepSavingResult.ERROR ? R.string.step_saving_error : R.string.step_saving_success;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    //TODO what happen if I forgot override this method? I think it's bad solution overide getScreenNumber in every activity
    @Override
    public int getScreenNumber() {
        return 0;
    }

    @Override
    public void incrementScreenNumber() {
        mScreenNumber++;
    }
}
