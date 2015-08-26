package com.epam.amtt.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Ivan_Bakach
 * @version on 21.07.2015
 */
public class FragmentInfoHelper {

    private static final String TAG = FragmentInfoHelper.class.getSimpleName();
    private static String sListFragments;
    public static String sCurrentArguments;

    public static void initFragmentsInfo(final Activity activity){
        sListFragments = "";
        sCurrentArguments = "";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity != null && activity instanceof FragmentActivity && ((FragmentActivity) activity).getSupportFragmentManager()!=null&&((FragmentActivity) activity).getSupportFragmentManager().getFragments() != null) {
                    for (Fragment fragment : ((FragmentActivity) activity).getSupportFragmentManager().getFragments()) {
                        if (fragment!=null&&fragment.isVisible() && fragment.getUserVisibleHint() && fragment.getView() != null && fragment.getView().getParent() != null && !fragment.getView().getParent().isLayoutRequested()) {
                            Bundle bundleArguments = fragment.getArguments();
                            if (bundleArguments != null) {
                                sCurrentArguments = fragment.getClass().getSimpleName() + ":\n" + FragmentInfoHelper.getArgumentsFromFragments(bundleArguments);
                            }
                            sListFragments += (fragment.getClass().getSimpleName() + ScreenshotHelper.BR_TAG);
                        }
                    }
                }
            }
        }, 1000);
    }
    private static String getArgumentsFromFragments(Bundle bundleArguments){
        String intDateArguments = bundleArguments.toString().trim().replaceAll("[}{\\[\\]]|Bundle", "");//clean
        String[] argumentsArray = intDateArguments.split(",");
        StringBuilder builder = new StringBuilder();
        for (String item:argumentsArray) {
            builder.append(item).append("\n");
        }
        return builder.toString();
    }

    public static void writeArgumentsFromFragments(String arguments) {
        FileOutputStream argumentsFile = null;
        try {
            argumentsFile = IOUtils.openFileOutput(LogManager.sArgumentsFragments);
            argumentsFile.write(arguments.getBytes());
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (argumentsFile != null) {
                try {
                    argumentsFile.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }


    public static CharSequence getActivityTitle(Activity activity){
        return activity !=null ? activity.getTitle() : null;
    }

    public static String getListFragments() {
        return sListFragments;
    }
}
