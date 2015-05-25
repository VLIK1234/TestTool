package amtt.epam.com.amtt.observer;

import android.content.pm.PackageManager;
import android.os.FileObserver;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amtt.epam.com.amtt.util.StepUtil;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.topbutton.view.TopButtonView;

/**
 * Created by Ivan_Bakach on 06.05.2015.
 */
public class AmttFileObserver extends FileObserver {
    private static final String TAG = "TAG";
    private static final String TIME_SCREENSHOT_PATTERN = "\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s[.]png";
    private static final String SCREENSHOT_PATTERN = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, TIME_SCREENSHOT_PATTERN);
    private static final long TIMEOUT_SCREENSHOT_CREATE = 3000L;
    public static final String SCREENSHOT_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    private String absolutePath;
    private static ArrayList<String> imageArray;

    public AmttFileObserver(String path) {
        super(path, FileObserver.ALL_EVENTS);
        absolutePath = path;
        imageArray = new ArrayList<>();
    }

    @Override
    public void onEvent(int event, String path) {
        if (path == null) {
            return;
        }
        //a new file or subdirectory was created under the monitored directory
        if ((FileObserver.CREATE & event) != 0) {
            Log.d(TAG, absolutePath + "/" + path + " is created\n");

            if (isNewScreenshot(path) && TopButtonView.getStartRecord()) {
                imageArray.add(absolutePath + "/" + path);
                ScheduledExecutorService worker =
                        Executors.newSingleThreadScheduledExecutor();
                final String createPath = path;
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            StepUtil.buildStepSaving(absolutePath + "/" + createPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
//                        TopButtonService.sendActionScreenshot(absolutePath + "/" + createPath);
                        TopButtonService.sendActionVisibleView();
                    }
                };
                worker.schedule(task, 1, TimeUnit.SECONDS);

            }
        }
        //a file or directory was opened
        if ((FileObserver.OPEN & event) != 0) {
            Log.d(TAG, path + " is opened\n");
        }
        //data was read from a file
        if ((FileObserver.ACCESS & event) != 0) {
            Log.d(TAG, absolutePath + "/" + path + " is accessed/read\n");
        }
        //data was written to a file
        if ((FileObserver.MODIFY & event) != 0) {
        }
        //someone has a file or directory open read-only, and closed it
        if ((FileObserver.CLOSE_NOWRITE & event) != 0) {
            Log.d(TAG, path + " is closed\n");
        }
        //someone has a file or directory open for writing, and closed it
        if ((FileObserver.CLOSE_WRITE & event) != 0) {
            Log.d(TAG, absolutePath + "/" + path + " is written and closed\n");
        }
        if ((FileObserver.DELETE & event) != 0) {
            Log.d(TAG, absolutePath + "/" + path + " is deleted\n");
        }
        //the monitored file or directory was deleted, monitoring effectively stops
        if ((FileObserver.DELETE_SELF & event) != 0) {
            Log.d(TAG, absolutePath + "/" + " is deleted\n");
        }
        //a file or subdirectory was moved from the monitored directory
        if ((FileObserver.MOVED_FROM & event) != 0) {
            Log.d(TAG, absolutePath + "/" + path + " is moved to somewhere " + "\n");
        }
        //a file or subdirectory was moved to the monitored directory
        if ((FileObserver.MOVED_TO & event) != 0) {
            Log.d(TAG, "File is moved to " + absolutePath + "/" + path + "\n");
        }
        //the monitored file or directory was moved; monitoring continues
        if ((FileObserver.MOVE_SELF & event) != 0) {
            Log.d(TAG, path + " is moved\n");
        }
        //Metadata (permissions, owner, timestamp) was changed explicitly
        if ((FileObserver.ATTRIB & event) != 0) {
            Log.d(TAG, absolutePath + "/" + path + " is changed (permissions, owner, timestamp)\n");
        }
    }

    private boolean isNewScreenshot(String path) {
        Pattern screenshotPattern = Pattern.compile(SCREENSHOT_PATTERN);
        Matcher matcherScreenshot = screenshotPattern.matcher(path);
        Pattern timePattern = Pattern.compile(TIME_SCREENSHOT_PATTERN);
        Matcher matcherTime = timePattern.matcher(path);
        if (matcherScreenshot.find() && matcherTime.find()) {
            long imageTime = System.currentTimeMillis();
            SimpleDateFormat imageDate = new SimpleDateFormat(SCREENSHOT_DATE_FORMAT);
            try {
                Date screenshotTime = imageDate.parse(matcherTime.group());
                return screenshotTime.getTime() - imageTime <= TIMEOUT_SCREENSHOT_CREATE;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ArrayList<String> getImageArray() {
        return imageArray;
    }
}
