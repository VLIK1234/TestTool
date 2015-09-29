package com.epam.amtt.test;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import junit.framework.Assert;

/**
 * @author IvanBakach
 * @version on 29.09.2015
 */
public class InjectionHelper{
    private static final String EXCEPTION_ANSWER_ACTION = "EXCEPTION_ANSWER";
    private static final String EXCEPTION_ANSWER_KEY = "answer";
    private static final int RECEIVER_TIMEOUT = 1000;

    private static final TestBroadcastReceiver receiver = new TestBroadcastReceiver();
    private static Application sApplication;
    private static Application.ActivityLifecycleCallbacks sLifecycleCallbacks;

    private InjectionHelper(){

    }

    public static void initInjection(Application application){
        setApplication(application);
        setUncaughtExceptionHandler();
        setBroadcastReceiver();
    }

    private static void setApplication(Application application) {
        sApplication = application;
        registerActivityLifecycleCallbacks(sApplication);
    }

    public static void setWindowCallback(Activity activity) {
        if (activity!=null&&activity.getWindow()!=null) {
            final Window.Callback windowCallback = activity.getWindow().getCallback();
            activity.getWindow().setCallback(new Window.Callback() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent event) {
                    return windowCallback.dispatchKeyEvent(event);
                }

                @Override
                public boolean dispatchKeyShortcutEvent(KeyEvent event) {
                    return windowCallback.dispatchKeyShortcutEvent(event);
                }

                @Override
                public boolean dispatchTouchEvent(MotionEvent event) {
                    return windowCallback.dispatchTouchEvent(event);
                }

                @Override
                public boolean dispatchTrackballEvent(MotionEvent event) {
                    return windowCallback.dispatchTrackballEvent(event);
                }

                @Override
                public boolean dispatchGenericMotionEvent(MotionEvent event) {
                    return windowCallback.dispatchGenericMotionEvent(event);
                }

                @Override
                public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
                    return windowCallback.dispatchPopulateAccessibilityEvent(event);
                }

                @Nullable
                @Override
                public View onCreatePanelView(int featureId) {
                    return windowCallback.onCreatePanelView(featureId);
                }

                @Override
                public boolean onCreatePanelMenu(int featureId, Menu menu) {
                    return windowCallback.onCreatePanelMenu(featureId, menu);

                }

                @Override
                public boolean onPreparePanel(int featureId, View view, Menu menu) {
                    return windowCallback.onPreparePanel(featureId, view, menu);
                }

                @Override
                public boolean onMenuOpened(int featureId, Menu menu) {
                    return windowCallback.onMenuOpened(featureId, menu);
                }

                @Override
                public boolean onMenuItemSelected(int featureId, MenuItem item) {
                    return windowCallback.onMenuItemSelected(featureId, item);
                }

                @Override
                public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
                    windowCallback.onWindowAttributesChanged(attrs);
                }

                @Override
                public void onContentChanged() {
                    windowCallback.onContentChanged();
                }

                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                    Log.d("onWindowFocusChanged", hasFocus + " hasFocus");
                    windowCallback.onWindowFocusChanged(hasFocus);
                }

                @Override
                public void onAttachedToWindow() {
                    windowCallback.onAttachedToWindow();
                }

                @Override
                public void onDetachedFromWindow() {
                    windowCallback.onDetachedFromWindow();
                }

                @Override
                public void onPanelClosed(int featureId, Menu menu) {
                    windowCallback.onPanelClosed(featureId, menu);
                }

                @Override
                public boolean onSearchRequested() {
                    return windowCallback.onSearchRequested();
                }

                //for support 23 api
//            @Override
//            public boolean onSearchRequested(SearchEvent searchEvent) {
//                return windowCallback.onSearchRequested(searchEvent);
//            }

                @Nullable
                @Override
                public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
                    return windowCallback.onWindowStartingActionMode(callback);
                }
//for support 23 api
//            @Nullable
//            @Override
//            public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
//                return windowCallback.onWindowStartingActionMode(callback, type);
//            }

                @Override
                public void onActionModeStarted(ActionMode mode) {
                    windowCallback.onActionModeStarted(mode);
                }

                @Override
                public void onActionModeFinished(ActionMode mode) {
                    windowCallback.onActionModeFinished(mode);
                }
            });
        }
    }

    private static void setUncaughtExceptionHandler(){
        final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(thread.getName(), ex.toString());
                FragmentInfoHelper.writeArgumentsFromFragments(FragmentInfoHelper.sCurrentArguments);
                Intent intent = new Intent();
                intent.setAction(EXCEPTION_ANSWER_ACTION);
                intent.putExtra(EXCEPTION_ANSWER_KEY, ex.getClass().getName());
                sApplication.sendBroadcast(intent);
                exceptionHandler.uncaughtException(thread, ex);
                LogManager.closeLogsWriter();
            }
        });
    }

    private static void registerActivityLifecycleCallbacks(Application application) {
        sLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                receiver.setActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (activity!=null&&receiver.mActivity!=null&&activity.getLocalClassName().equals(receiver.mActivity.getLocalClassName())) {
                    receiver.setActivity(null);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
        application.registerActivityLifecycleCallbacks(sLifecycleCallbacks);
    }

    private static void unregisterActivityLifecycleCallbacks() {
        sApplication.unregisterActivityLifecycleCallbacks(sLifecycleCallbacks);
    }

    private static void setBroadcastReceiver() {
        IntentFilter filterReceiver = new IntentFilter();
        filterReceiver.addCategory(Intent.CATEGORY_DEFAULT);
        filterReceiver.addAction(TestBroadcastReceiver.PING_ANSWER);
        filterReceiver.addAction(TestBroadcastReceiver.CLOSE_TEST);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_SCREENSHOT);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_LOGS);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_ONLY_INFO);
        sApplication.registerReceiver(receiver, filterReceiver);
        receiver.setCloseUnitTest(false);

        while (!receiver.needCloseUnitTest()) {
            try {
                Thread.sleep(RECEIVER_TIMEOUT);
                if (receiver.needCloseUnitTest()) {
                    sApplication.unregisterReceiver(receiver);
                    unregisterActivityLifecycleCallbacks();
                    LogManager.closeLogsWriter();
                    Assert.assertTrue(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
