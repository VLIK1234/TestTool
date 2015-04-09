package amtt.epam.com.amtt.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.BaseActivity;
import amtt.epam.com.amtt.view.TopButtonView;

/**
 * Created by Ivan_Bakach on 20.03.2015.
 */
public class TopButtonService extends Service {

    public static final String ACTION_SHOW = "SHOW";
    public static final String ACTION_CLOSE = "CLOSE";
    private static final String LOG_TAG = "Log";
    public static final int ID = 7;
    public static final String ACTION_HIDE_VIEW = "HIDE_VIEW";
    private int xInitPosition;
    private int yInitPosition;
    private TopButtonView view;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private boolean isViewAdd = false;
    private NotificationCompat.Action action;
    private NotificationCompat.Builder builder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        xInitPosition = displayMetrics.widthPixels / 2;
        yInitPosition = displayMetrics.heightPixels / 2;
        intitLayoutParams();
        initView();
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

    private void initView() {
        view = new TopButtonView(getBaseContext(), layoutParams);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentATS = new Intent(BaseActivity.ACTION_SAVE_STEP);
                sendBroadcast(intentATS);
            }
        });
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
        if (!isViewAdd) {
            wm.addView(view, layoutParams);
            isViewAdd = true;
        }
    }

    public final void close() {
        if (view != null && isViewAdd) {
            isViewAdd = false;
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
            view = null;
        }
        stopSelf();
    }

    public final void setVisibilityView() {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            action.icon = R.drawable.ic_stat_action_visibility;
            action.title = getString(R.string.button_show);
            startForeground(ID, builder.build());
        } else {
            view.setVisibility(View.VISIBLE);
            action.icon = R.drawable.ic_stat_action_visibility_off;
            action.title = getString(R.string.button_hide);
            startForeground(ID, builder.build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            String action = intent.getAction();

            if (ACTION_SHOW.equals(action)) {
                show();
                showNotification();
            } else if (ACTION_CLOSE.equals(action)) {
                close();
            } else if (ACTION_HIDE_VIEW.equals(action)) {
                setVisibilityView();
            }
        } else {
            Log.w(LOG_TAG, "Tried to onStartCommand() with a null intent.");
        }
        return START_NOT_STICKY;
    }

    private void showNotification() {
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("AMTT")
                .setOngoing(true)
                .setContentText("Button-assistant is running.");

        action = new NotificationCompat.Action(
                R.drawable.ic_stat_action_visibility_off,
                getString(R.string.button_hide),
                PendingIntent.getService(this, 0, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_HIDE_VIEW), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.addAction(action);
        startForeground(ID, builder.build());
    }

}
