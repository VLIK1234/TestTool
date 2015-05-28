package amtt.epam.com.amtt.topbutton.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.io.File;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.MainActivity;
import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.topbutton.view.TopButtonView;

/**
 * Created by Ivan_Bakach on 20.03.2015.
 */
public class TopButtonService extends Service{

    public static final String ACTION_START = "amtt.epam.com.amtt.topbutton.service.SHOW";
    public static final String ACTION_CLOSE = "amtt.epam.com.amtt.topbutton.service.CLOSE";
    private static final String TAG = "Log";
    public static final int NOTIFICATION_ID = 7;
    //don't use REQUEST_CODE = 0 - it's broke action in notification for some device
    public static final int REQUEST_CODE = 1;
    public static final String ACTION_SHOW_SCREEN = "amtt.epam.com.amtt.topbutton.service.SHOW_SCREEN";
    public static final String ACTION_HIDE_VIEW = "amtt.epam.com.amtt.topbutton.service.HIDE_VIEW";
    public static final String ACTION_SHOW_VIEW = "amtt.epam.com.amtt.topbutton.service.SHOW_VIEW";
    public static final String PATH_TO_SCREEENSHOT_KEY = "PATH_TO_SCREENSHOT";
    private static final String SCREENSHOTS_DIR_NAME = "Screenshots";
    private int xInitPosition;
    private int yInitPosition;
    private TopButtonView view;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private boolean isViewAdd = false;
    private NotificationCompat.Action action;
    private NotificationCompat.Builder builder;
    private AmttFileObserver fileObserver;
    //bellow field for cap code and will be delete after do work realization
    private static Context context;

    public void showScreenInGallery(String pathToScreenshot) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(pathToScreenshot);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
    }

    public static void sendActionShowScreenInGallery(String pathToScreenshot) {
        Intent intent = new Intent(context, TopButtonService.class).setAction(ACTION_SHOW_SCREEN);
        intent.putExtra(PATH_TO_SCREEENSHOT_KEY, pathToScreenshot);
        context.startService(intent);
    }

    public static void sendActionShowButton() {
        Intent intentShowView = new Intent(context, TopButtonService.class).setAction(TopButtonService.ACTION_SHOW_VIEW);
        intentShowView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startService(intentShowView);
    }

    public static void start(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_START));
    }

    public static void close(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_CLOSE));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        xInitPosition = displayMetrics.widthPixels / 2;
        yInitPosition = displayMetrics.heightPixels / 2;
        initLayoutParams();
        view = new TopButtonView(getBaseContext(), layoutParams);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), SCREENSHOTS_DIR_NAME);
//        File file = new File(Environment.getExternalStoragePublicDirectory("DCIM"), SCREENSHOTS_DIR_NAME);
        file.mkdirs();
        Log.d(TAG, file.getPath());
        fileObserver = new AmttFileObserver(file.getAbsolutePath());
        fileObserver.startWatching();
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
                    fileObserver.stopWatching();
                    closeService();
                    break;
                case ACTION_HIDE_VIEW:
                    changeStateNotificationAction();
                    break;
                case ACTION_SHOW_VIEW:
                    changeStateNotificationAction();
                    break;
                case ACTION_SHOW_SCREEN:
                    Bundle extra = intent.getExtras();
                    if (extra != null) {
                        showScreenInGallery(extra.getString(PATH_TO_SCREEENSHOT_KEY));
                    }
                    break;
            }
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void initLayoutParams() {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;

        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, xInitPosition, yInitPosition, WindowManager.LayoutParams.TYPE_PHONE,
                flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP | Gravity.START;
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
            wm.removeView(view);
            view = null;
        }
        stopSelf();
    }

    private void showNotification() {
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setOngoing(true)
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(PendingIntent.getActivity(getBaseContext(), NOTIFICATION_ID, new Intent(getBaseContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));


        action = new NotificationCompat.Action(
                R.drawable.ic_stat_action_visibility_off,
                getString(R.string.label_hide),
                PendingIntent.getService(getBaseContext(), REQUEST_CODE, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_HIDE_VIEW), PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationCompat.Action closeService = new NotificationCompat.Action(
                R.drawable.ic_close_service,
                getString(R.string.label_close),
                PendingIntent.getService(getBaseContext(), REQUEST_CODE, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_CLOSE), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.addAction(action);
        builder.addAction(closeService);
        startForeground(NOTIFICATION_ID, builder.build());
    }

    public final void changeStateNotificationAction() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            view.buttonsBar.setVisibility(View.GONE);
            action.icon = R.drawable.ic_stat_action_visibility;
            action.title = getString(R.string.label_show);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            view.setVisibility(View.VISIBLE);
            action.icon = R.drawable.ic_stat_action_visibility_off;
            action.title = getString(R.string.label_hide);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
