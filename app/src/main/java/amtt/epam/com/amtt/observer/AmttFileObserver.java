package amtt.epam.com.amtt.observer;

import android.os.FileObserver;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * Created by Ivan_Bakach on 06.05.2015.
 */
public class AmttFileObserver extends FileObserver {
    private static final String TAG = "TAG";
    private static final String TIME_SCREENSHOT_PATTERN = "\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}";
    private static final String SCREENSHOT_PATTERN = "Screenshot_" + TIME_SCREENSHOT_PATTERN + "[.]png";
    private static final long TIMEOUT = 300000L;
    private String absolutePath;
    public AmttFileObserver(String path) {
        super(path, FileObserver.ALL_EVENTS);
        absolutePath = path;
    }
    @Override
    public void onEvent(int event, String path) {
        if (path == null) {
            return;
        }
        //a new file or subdirectory was created under the monitored directory
        if ((FileObserver.CREATE & event)!=0) {
            Log.d(TAG, absolutePath + "/" + path + " is created\n");

            if(isScreenshot(path)){
                TopButtonService.sendActionScreenshot(absolutePath + "/" + path);
            }
        }
        //a file or directory was opened
        if ((FileObserver.OPEN & event)!=0) {
            Log.d(TAG, path + " is opened\n");
        }
        //data was read from a file
        if ((FileObserver.ACCESS & event)!=0) {
            Log.d(TAG, absolutePath + "/" + path + " is accessed/read\n");
        }
        //data was written to a file
        if ((FileObserver.MODIFY & event)!=0) {
        }
        //someone has a file or directory open read-only, and closed it
        if ((FileObserver.CLOSE_NOWRITE & event)!=0) {
            Log.d(TAG, path + " is closed\n");
        }
        //someone has a file or directory open for writing, and closed it
        if ((FileObserver.CLOSE_WRITE & event)!=0) {
            Log.d(TAG, absolutePath + "/" + path + " is written and closed\n");
        }
        if ((FileObserver.DELETE & event)!=0) {
            Log.d(TAG, absolutePath + "/" + path + " is deleted\n");
        }
        //the monitored file or directory was deleted, monitoring effectively stops
        if ((FileObserver.DELETE_SELF & event)!=0) {
            Log.d(TAG, absolutePath + "/" + " is deleted\n");
        }
        //a file or subdirectory was moved from the monitored directory
        if ((FileObserver.MOVED_FROM & event)!=0) {
            Log.d(TAG, absolutePath + "/" + path + " is moved to somewhere " + "\n");
        }
        //a file or subdirectory was moved to the monitored directory
        if ((FileObserver.MOVED_TO & event)!=0) {
            Log.d(TAG, "File is moved to " + absolutePath + "/" + path + "\n");
        }
        //the monitored file or directory was moved; monitoring continues
        if ((FileObserver.MOVE_SELF & event)!=0) {
            Log.d(TAG, path + " is moved\n");
        }
        //Metadata (permissions, owner, timestamp) was changed explicitly
        if ((FileObserver.ATTRIB & event)!=0) {
            Log.d(TAG, absolutePath + "/" + path + " is changed (permissions, owner, timestamp)\n");
        }
    }
    private boolean isScreenshot(String path){
        Pattern screenshotPattern = Pattern.compile(SCREENSHOT_PATTERN);
        Matcher matcherScreenshot = screenshotPattern.matcher(path);
        Log.d(TAG,path);
        if(matcherScreenshot.find()){
            Pattern time = Pattern.compile(TIME_SCREENSHOT_PATTERN);
            Log.d(TAG,path+" current");
        }
//      SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//      return matcherScreenshot.find()&&(System.currentTimeMillis()-time.parse(matcherTime.group(0)).getTime() <= TIMEOUT);
        return matcherScreenshot.find();
    }
}
