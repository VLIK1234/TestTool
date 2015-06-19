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
import amtt.epam.com.amtt.app.SettingActivity;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.topbutton.view.TopButtonView;

/**
 @author Ivan_Bakach
 @version on 20.03.2015
 */

public class TopButtonService extends Service{

    private static final String SCREENSHOTS_DIR_NAME = "Screenshots";
    private static final String TAG = "Log";
    public static final String ACTION_CLOSE = "amtt.epam.com.amtt.topbutton.service.CLOSE";
    public static final String ACTION_CHANGE_VISIBILITY_TOPBUTTON = "amtt.epam.com.amtt.topbutton.service.ACTION_CHANGE_VISIBILITY_TOPBUTTON";
    public static final String ACTION_CHANGE_NOTIFICATION_BUTTON = "amtt.epam.com.amtt.topbutton.service.ACTION_CHANGE_NOTIFICATION_BUTTON";
    public static final String ACTION_SHOW_SCREEN = "amtt.epam.com.amtt.topbutton.service.SHOW_SCREEN";
    public static final String ACTION_START = "amtt.epam.com.amtt.topbutton.service.START";
    public static final String VISIBILITY_TOP_BUTTON = "amtt.epam.com.amtt.topbutton.service.VISIBILITY_TOP_BUTTON";
    public static final String PATH_TO_SCREEENSHOT_KEY = "PATH_TO_SCREENSHOT";
    //don't use REQUEST_CODE = 0 - it's broke mActionNotificationCompat in notification for some device
    public static final int REQUEST_CODE = 1;
    public static final int NOTIFICATION_ID = 7;
    //bellow field for cap code and will be delete after do work realization
    private static Context mContext;
    private AmttFileObserver mFileObserver;
    private NotificationCompat.Action mActionNotificationCompat;
    private NotificationCompat.Builder mBuilderNotificationCompat;
    private static TopButtonView mTopButtonView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean isViewAdd = false;
    private int mXInitPosition;
    private int mYInitPosition;

    public void showScreenInGallery(String pathToScreenshot) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(pathToScreenshot);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
    }

    private static void sendActionChangeNotificationButton() {
        Intent intentHideView = new Intent(mContext, TopButtonService.class).setAction(TopButtonService.ACTION_CHANGE_NOTIFICATION_BUTTON);
        intentHideView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.getApplicationContext().startService(intentHideView);
    }

    public static void sendActionChangeVisibilityTopbutton(boolean visible) {
        Intent intentHideView = new Intent(mContext, TopButtonService.class).setAction(TopButtonService.ACTION_CHANGE_VISIBILITY_TOPBUTTON);
        intentHideView.putExtra(VISIBILITY_TOP_BUTTON, visible);
        intentHideView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.getApplicationContext().startService(intentHideView);
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
        mContext = getBaseContext();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        mXInitPosition = displayMetrics.widthPixels / 2;
        mYInitPosition = displayMetrics.heightPixels / 2;
        initLayoutParams();
        mTopButtonView = new TopButtonView(getBaseContext(), mLayoutParams);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), SCREENSHOTS_DIR_NAME);
        Log.d(TAG, file.getPath());
        mFileObserver = new AmttFileObserver(file.getAbsolutePath());
        mFileObserver.startWatching();
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
                    mFileObserver.stopWatching();
                    closeService();
                    break;
                case ACTION_CHANGE_NOTIFICATION_BUTTON:
                    changeStateNotificationAction();
                    break;
                case ACTION_CHANGE_VISIBILITY_TOPBUTTON:
                    Bundle extraBoolean = intent.getExtras();
                    if (extraBoolean!=null) {
                        setTopButtonVisible(extraBoolean.getBoolean(VISIBILITY_TOP_BUTTON));
                    }
                    break;
                case ACTION_SHOW_SCREEN:
                    sendActionChangeNotificationButton();
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

        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, mXInitPosition, mYInitPosition, WindowManager.LayoutParams.TYPE_PHONE,
                flags, PixelFormat.TRANSLUCENT);
        mLayoutParams.gravity = Gravity.TOP | Gravity.START;
    }

    private void addView() {
        if (!isViewAdd) {
            mWindowManager.addView(mTopButtonView, mLayoutParams);
            isViewAdd = true;
        }
    }

    private void closeService() {
        if (mTopButtonView != null && isViewAdd) {
            StepUtil.clearAllStep();
            isViewAdd = false;
            mTopButtonView.getButtonsBar().setIsRecordStarted(false);
            mWindowManager.removeViewImmediate(mTopButtonView);
            mWindowManager.removeViewImmediate(mTopButtonView.getButtonsBar());
            mTopButtonView = null;
        }
        stopSelf();
    }

    private void showNotification() {
        mBuilderNotificationCompat = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setOngoing(true)
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(PendingIntent.getActivity(getBaseContext(), NOTIFICATION_ID, new Intent(getBaseContext(), SettingActivity.class),PendingIntent.FLAG_CANCEL_CURRENT));


        mActionNotificationCompat = new NotificationCompat.Action(
                R.drawable.ic_stat_action_visibility_off,
                getString(R.string.label_hide),
                PendingIntent.getService(getBaseContext(), REQUEST_CODE, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_CHANGE_NOTIFICATION_BUTTON), PendingIntent.FLAG_CANCEL_CURRENT));

        NotificationCompat.Action closeService = new NotificationCompat.Action(
                R.drawable.ic_close_service,
                getString(R.string.label_close),
                PendingIntent.getService(getBaseContext(), REQUEST_CODE, new Intent(getBaseContext(), TopButtonService.class).setAction(ACTION_CLOSE), PendingIntent.FLAG_CANCEL_CURRENT));

        mBuilderNotificationCompat.addAction(mActionNotificationCompat);
        mBuilderNotificationCompat.addAction(closeService);
        startForeground(NOTIFICATION_ID, mBuilderNotificationCompat.build());
    }

    private void changeStateNotificationAction() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mTopButtonView.getVisibility() == View.VISIBLE) {
            mTopButtonView.setVisibility(View.GONE);
            mTopButtonView.getButtonsBar().hide();
            mActionNotificationCompat.icon = R.drawable.ic_stat_action_visibility;
            mActionNotificationCompat.title = getString(R.string.label_show);
            notificationManager.notify(NOTIFICATION_ID, mBuilderNotificationCompat.build());
        } else {
            mTopButtonView.setVisibility(View.VISIBLE);
            mActionNotificationCompat.icon = R.drawable.ic_stat_action_visibility_off;
            mActionNotificationCompat.title = getString(R.string.label_hide);
            notificationManager.notify(NOTIFICATION_ID, mBuilderNotificationCompat.build());
        }
    }

    private void setTopButtonVisible(boolean visible) {
        if (visible) {
            if (mTopButtonView.getVisibility() == View.GONE) {
                changeStateNotificationAction();
            }
        }else {
            if (mTopButtonView.getVisibility() == View.VISIBLE) {
                changeStateNotificationAction();
            }
        }
    }
}
