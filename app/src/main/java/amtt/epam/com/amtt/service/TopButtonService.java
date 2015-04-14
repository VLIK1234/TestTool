package amtt.epam.com.amtt.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class TopButtonService extends Service{

    public static final String ACTION_START = "SHOW";
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

    public static SharedPreferences setting;
    public static final String SCREEN_NUMBER = "Screen number";
    public static final String NUMBER = "Number";
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

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
        initLayoutParams();
        initView();

        //TODO change on real realization
        boolean isAccess = false;
        setting = getBaseContext().getSharedPreferences(SCREEN_NUMBER, Context.MODE_PRIVATE);
        setting.edit().putBoolean(NUMBER, isAccess).apply();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Log.e(LOG_TAG, "Shared Preference is changed");
                view.buttonAuth.setBackgroundResource(R.drawable.button_logout);
                view.buttonBugRep.setBackgroundResource(R.drawable.button_bug_rep);
                view.buttonBugRep.setEnabled(true);
                view.buttonUserInfo.setBackgroundResource(R.drawable.button_info);
                view.buttonUserInfo.setEnabled(true);
            }
        };
        setting.registerOnSharedPreferenceChangeListener(listener);
    }

    private void initLayoutParams() {
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
                Intent intentSendAction = new Intent(BaseActivity.ACTION_SAVE_STEP);
                sendBroadcast(intentSendAction);
            }
        });
    }

    public static void start(Context context) {
        context.startService(getShowIntent(context));
    }

    public static void close(Context context) {
        context.startService(getCloseIntent(context));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            String action = intent.getAction();

            if (ACTION_START.equals(action)) {
                addView();
                showNotification();
            } else if (ACTION_CLOSE.equals(action)) {
                closeService();
            } else if (ACTION_HIDE_VIEW.equals(action)) {
                changeVisibilityView();
            }
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private static Intent getShowIntent(Context context) {
        return new Intent(context, TopButtonService.class).setAction(ACTION_START);
    }

    private static Intent getCloseIntent(Context context) {
        return new Intent(context, TopButtonService.class).setAction(ACTION_CLOSE);
    }

    private void addView() {
        if (!isViewAdd) {
            wm.addView(view, layoutParams);
            isViewAdd = true;
        }
    }

    private void closeService() {
        if (view != null && isViewAdd) {
            isViewAdd = false;
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
            view = null;
        }
        stopSelf();
    }

    private void showNotification() {
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setOngoing(true)
                .setContentText(getString(R.string.notification_text));

        action = new NotificationCompat.Action(
                R.drawable.ic_stat_action_visibility_off,
                getString(R.string.button_hide),
                PendingIntent.getService(this, 0, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_HIDE_VIEW), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.addAction(action);
        startForeground(ID, builder.build());
    }

    public final void changeVisibilityView() {
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
}
