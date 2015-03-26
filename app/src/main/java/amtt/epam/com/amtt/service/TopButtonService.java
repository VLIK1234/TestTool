package amtt.epam.com.amtt.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import amtt.epam.com.amtt.app.BaseActivity;
import amtt.epam.com.amtt.view.TopButtonView;

/**
 * Created by Ivan_Bakach on 20.03.2015.
 */
public class TopButtonService extends Service {

    public static final String ACTION_SHOW = "SHOW";
    public static final String ACTION_CLOSE = "CLOSE";
    private DisplayMetrics displayMetrics;
    private int xInitPosition;
    private int yInitPosition;
    private TopButtonView view;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private final String LOG_TAG = "myLogs";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        xInitPosition = displayMetrics.widthPixels / 2;
        yInitPosition = displayMetrics.heightPixels / 2;
        intitLayoutParams();
        wm.getDefaultDisplay();
        view = new TopButtonView(getBaseContext(), wm, layoutParams);

    }

    private void intitLayoutParams() {
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.x = xInitPosition;
        layoutParams.y = yInitPosition;
    }

    public static Intent getShowIntent(Context context) {
        return new Intent(context, TopButtonService.class).setAction(ACTION_SHOW);
    }

    public static Intent getCloseIntent(Context context) {
        return new Intent(context, TopButtonService.class).setAction(ACTION_CLOSE);
    }

    public static void show(Context context) {
        context.startService(getShowIntent(context));
    }

    public static void close(Context context) {
        context.startService(getCloseIntent(context));
    }

    public final void show() {
        wm.addView(view, layoutParams);
    }

    public final void close(Intent name) {
        if (view != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
            view = null;
        }
        stopService(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                Intent intentATS = new Intent(BaseActivity.ACTION_TAKE_SCREENSHOT);
                sendBroadcast(intentATS);
                view.setVisibility(View.VISIBLE);
            }
        });

        if (intent != null) {
            String action = intent.getAction();

            if (ACTION_SHOW.equals(action)) {
                show();
            } else if (ACTION_CLOSE.equals(action)) {
                close(intent);
            }
        } else {
            Log.w(LOG_TAG, "Tried to onStartCommand() with a null intent.");
        }
        return START_NOT_STICKY;
    }

}
