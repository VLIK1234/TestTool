package amtt.epam.com.amtt.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import amtt.epam.com.amtt.R;
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
    public static final String ACTION_AUTH_SUCCESS = "AUTHORIZATION_SUCCESS";
    private int xInitPosition;
    private int yInitPosition;
    private TopButtonView view;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private boolean isViewAdd = false;
    private NotificationCompat.Action action;
    private NotificationCompat.Builder builder;

    public static void start(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_START));
    }

    public static void close(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_CLOSE));
    }

    public static void authSuccess(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_AUTH_SUCCESS));
    }

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
        view = new TopButtonView(getBaseContext(), layoutParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_START:
                    addView();
                    showNotification();
                    break;
                case ACTION_CLOSE:
                    closeService();
                    break;
                case ACTION_HIDE_VIEW:
                    changeStateNotificationAction();
                    break;
                case ACTION_AUTH_SUCCESS:
                    changeUiAuthSuccess();
                    break;
            }
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
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

    private void changeUiAuthSuccess(){
        view.buttonAuth.setAuthStatee(true);
        view.buttonBugRep.setEnabled(true);
        view.buttonUserInfo.setEnabled(true);
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

        NotificationCompat.Action closeService = new NotificationCompat.Action(
                R.drawable.ic_close_service,
                getString(R.string.button_close_service),
                PendingIntent.getService(this, 0, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_CLOSE), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.addAction(action);
        builder.addAction(closeService);
        startForeground(ID, builder.build());
    }

    public final void changeStateNotificationAction() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            action.icon = R.drawable.ic_stat_action_visibility;
            action.title = getString(R.string.button_show);
            notificationManager.notify(ID, builder.build());
        } else {
            view.setVisibility(View.VISIBLE);
            action.icon = R.drawable.ic_stat_action_visibility_off;
            action.title = getString(R.string.button_hide);
            notificationManager.notify(ID, builder.build());
        }
    }
}
