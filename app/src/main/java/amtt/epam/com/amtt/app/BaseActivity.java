package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class BaseActivity extends Activity {

    public static final String LOG_TAG = "TAG";
    public final static String ACTION_TAKE_SCREENSHOT = "amtt.epam.com.amtt.app.TAKESCREENSHOT";

    protected BroadcastReceiver br;

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
        //TODO why do we need to recreate receiver every time when onResume is called?
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_TAKE_SCREENSHOT)) {
                    Log.d(LOG_TAG, getLocalClassName());
                    //TODO You need to make screenshot of current visible app, not your activity
                    View rootView = getWindow().getDecorView();
                    rootView.setDrawingCacheEnabled(true);
                    //TODO setDrawingCacheEnabled(false) wasn't called, do we really need to call?
                    Bitmap bitmap = rootView.getDrawingCache();
                    Rect rect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    final String cachePath =
                            Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? getExternalCacheDir().getPath() :
                                    getCacheDir().getPath();
                    //TODO commented line, feature is not ready for master
                    //new ImageSavingTask((BaseActivity) context, bitmap, rect, cachePath).execute();
                    Toast.makeText(context, cachePath, Toast.LENGTH_SHORT).show();
                }
            }
        };
        //TODO try to use correct names to be able to search items later
        IntentFilter intFilt = new IntentFilter(ACTION_TAKE_SCREENSHOT);
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    //TODO remove unused code or put comment why do we need it in master branch
//    @Override
//    public void onImageSaved(ImageSavingResult result) {
//        BaseStorage.setNumber(BaseStorage.getNumber() + 1);
//        int resultMessage = result == ImageSavingResult.ERROR ? R.string.image_saving_error : R.string.image_saving_success;
//        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public int getScreenNumber() {
//        return BaseStorage.getNumber();
//    }

    public void showProgress(boolean show) {
        findViewById(getProgressViewId()).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected int getProgressViewId() {
        return R.id.progress;
    }
}
