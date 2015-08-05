package amtt.epam.com.amtt.topbutton.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.helper.NotificationIdConstant;
import amtt.epam.com.amtt.ui.activities.SettingActivity;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.topbutton.view.TopButtonView;
import amtt.epam.com.amtt.util.TestUtil;

/**
 @author Ivan_Bakach
 @version on 20.03.2015
 */

public class TopButtonService extends Service{

    private static final String ACTION_CLOSE = "amtt.epam.com.amtt.topbutton.service.CLOSE";
    private static final String ACTION_CHANGE_VISIBILITY_TOPBUTTON = "amtt.epam.com.amtt.topbutton.service.ACTION_CHANGE_VISIBILITY_TOPBUTTON";
    private static final String ACTION_CHANGE_NOTIFICATION_BUTTON = "amtt.epam.com.amtt.topbutton.service.ACTION_CHANGE_NOTIFICATION_BUTTON";
    private static final String ACTION_SHOW_SCREEN = "amtt.epam.com.amtt.topbutton.service.SHOW_SCREEN";
    private static final String ACTION_START = "amtt.epam.com.amtt.topbutton.service.START";
    private static final String ACTION_STOP_RECORD = "amtt.epam.com.amtt.topbutton.service.STOP_RECORD";
    private static final String VISIBILITY_TOP_BUTTON = "amtt.epam.com.amtt.topbutton.service.VISIBILITY_TOP_BUTTON";
    private static final String PATH_TO_SCREEENSHOT_KEY = "PATH_TO_SCREENSHOT";
    //don't use REQUEST_CODE = 0 - it's broke mActionNotificationCompat in notification for some device
    private static final int REQUEST_CODE = 1;
    //bellow field for cap code and will be delete after do work realization
    private NotificationCompat.Action mActionNotificationCompat;
    private NotificationCompat.Builder mBuilderNotificationCompat;
    private static TopButtonView mTopButtonView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean isViewAdd = false;
    private int mXInitPosition;
    private int mYInitPosition;

    private void showScreenInGallery(String pathToScreenshot) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(pathToScreenshot);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
    }

    private static void sendActionChangeNotificationButton() {
        Intent intentHideView = new Intent(AmttApplication.getContext(), TopButtonService.class).setAction(TopButtonService.ACTION_CHANGE_NOTIFICATION_BUTTON);
        intentHideView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AmttApplication.getContext().startService(intentHideView);
    }

    public static void sendActionChangeTopButtonVisibility(boolean visible) {
        Intent intentHideView = new Intent(AmttApplication.getContext(), TopButtonService.class);
        intentHideView.setAction(TopButtonService.ACTION_CHANGE_VISIBILITY_TOPBUTTON);
        intentHideView.putExtra(VISIBILITY_TOP_BUTTON, visible);
        intentHideView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AmttApplication.getContext().startService(intentHideView);
    }

    public static void start(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_START));
    }

    public static void close(Context context) {
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_CLOSE));
    }

    public static void stopRecord(Context context){
        context.startService(new Intent(context, TopButtonService.class).setAction(ACTION_STOP_RECORD));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        mXInitPosition = displayMetrics.widthPixels / 2;
        mYInitPosition = displayMetrics.heightPixels / 2;
        initLayoutParams();
        mTopButtonView = new TopButtonView(getBaseContext(), mLayoutParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_START:
                    addView();
                    TestUtil.restartTest();
                    checkCountTestProject();
                    break;
                case ACTION_CLOSE:
                    TestUtil.closeTest();
                    StepUtil.removeAllAttachFile();
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
                case ACTION_STOP_RECORD:
                    stopRecord();
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
            StepUtil.clearAllSteps();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
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
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setContentText(getString(R.string.notification_text))
                .setGroup(getString(R.string.label_amtt_system_group_notification))
                .setContentIntent(PendingIntent.getActivity(getBaseContext(), NotificationIdConstant.MAIN_AMTT, new Intent(getBaseContext(), SettingActivity.class),PendingIntent.FLAG_CANCEL_CURRENT));


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
        startForeground(NotificationIdConstant.MAIN_AMTT, mBuilderNotificationCompat.build());
    }

    private void changeStateNotificationAction() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mTopButtonView.getVisibility() == View.VISIBLE) {
            if (mTopButtonView.getButtonsBar()!=null) {
                mTopButtonView.getButtonsBar().hide();
            }
            mTopButtonView.setVisibility(View.GONE);
            mActionNotificationCompat.icon = R.drawable.ic_stat_action_visibility;
            mActionNotificationCompat.title = getString(R.string.label_show);
            notificationManager.notify(NotificationIdConstant.MAIN_AMTT, mBuilderNotificationCompat.build());
        } else {
            mTopButtonView.setVisibility(View.VISIBLE);
            mActionNotificationCompat.icon = R.drawable.ic_stat_action_visibility_off;
            mActionNotificationCompat.title = getString(R.string.label_hide);
            notificationManager.notify(NotificationIdConstant.MAIN_AMTT, mBuilderNotificationCompat.build());
        }
    }

    private void setTopButtonVisible(boolean visible) {
        if (visible) {
            if (mTopButtonView!=null&&mTopButtonView.getVisibility() == View.GONE) {
                changeStateNotificationAction();
            }
        }else {
            if (mTopButtonView!=null&&mTopButtonView.getVisibility() == View.VISIBLE) {
                changeStateNotificationAction();
            }
        }
    }

    private void stopRecord(){
        mTopButtonView.getButtonsBar().setIsRecordStarted(false);
        StepUtil.clearAllSteps();
    }

    private void checkCountTestProject() {
        if (TestUtil.getTestedApps().length>1) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(getBaseContext(), "You have many project. Please choose right tested project",Toast.LENGTH_LONG).show();
        }
    }
}
