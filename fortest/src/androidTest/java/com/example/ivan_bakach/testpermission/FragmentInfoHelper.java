package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Ivan_Bakach
 * @version on 21.07.2015
 */
public class FragmentInfoHelper {
    public static String sListFragments;
    public static String sCurrentArguments;
    public static void initFragmentsInfo(final Activity activity){
        sListFragments = "";
        sCurrentArguments = "";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity != null && activity instanceof FragmentActivity && ((FragmentActivity) activity).getSupportFragmentManager().getFragments() != null) {
                    for (Fragment fragment : ((FragmentActivity) activity).getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible() && fragment.getUserVisibleHint() && fragment.getView() != null && fragment.getView().getParent() != null && !fragment.getView().getParent().isLayoutRequested()) {
                            Bundle bundleArguments = fragment.getArguments();
                            if (bundleArguments != null) {
                                sCurrentArguments = fragment.getClass().getSimpleName() + "\n" + FragmentInfoHelper.getArgumentsFromFragments(bundleArguments);
                            }
                            sListFragments += (fragment.getClass().getSimpleName() + "<br/>");
                        }
                    }
                }
            }
        }, 1000);
    }
    public static String getArgumentsFromFragments(Bundle bundleArguments){
        String intDateArguments = bundleArguments.toString().replaceAll("[}{\\[\\]]|Bundle", "");//clean
        String[] argumentsArray = intDateArguments.split(",");
        StringBuilder builder = new StringBuilder();
        for (String item:argumentsArray) {
            builder.append(item).append("\n");
        }
        return builder.toString();
    }

    public static void writeArgumentsFromFragments(String arguments){
        try {
            FileOutputStream argumentsFile = IOUtils.openFileOutput(LogManger.sArgumentsFragments);
            argumentsFile.write(arguments.getBytes());
            argumentsFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getListFragments() {
        return sListFragments;
    }

    public static void setListFragments(String listFragments) {
        sListFragments = listFragments;
    }
}
