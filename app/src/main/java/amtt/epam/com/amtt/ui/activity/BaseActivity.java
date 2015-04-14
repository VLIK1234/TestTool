package amtt.epam.com.amtt.ui.activity;

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

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class BaseActivity extends Activity {

    public static final String LOG_TAG = "TAG";
    public final static String ACTION_TAKE_SCREENSHOT = "amtt.epam.com.amtt.ui.app.TAKESCREENSHOT";

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
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_TAKE_SCREENSHOT)) {
                    Log.d(LOG_TAG, getLocalClassName());
                    View rootView = getWindow().getDecorView();
                    rootView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = rootView.getDrawingCache();
                    Rect rect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    final String cachePath =
                            Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? getExternalCacheDir().getPath() :
                                    getCacheDir().getPath();
                    //new ImageSavingTask((BaseActivity) context, bitmap, rect, cachePath).execute();
                    Toast.makeText(context, cachePath, Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(ACTION_TAKE_SCREENSHOT);
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

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
}
