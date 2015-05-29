package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.util.Logger;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
@author Iryna Monchanka
@version on 27.05.2015
 */

public class ScreenshotManager {

    private final String TAG = this.getClass().getSimpleName();
    private static ArrayList<String> screenArray;
    private static ScreenshotManager mInstance;
    private List<Attachment> screenshotList;
    private String screenPath;

    public static ScreenshotManager getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenshotManager();
        }
        return mInstance;
    }

    public void setScreenshotList() {
        screenArray = new ArrayList<>();
        screenPath = Environment.getExternalStorageDirectory().getPath();
        List<File> files = getListFiles(new File( screenPath+ "/Pictures"));
        for (int i = 0; i < files.size(); i++) {
            File screenshot = files.get(i);
            screenArray.add(screenshot.getAbsolutePath());
            Logger.d(TAG, screenshot.getAbsolutePath());
        }
    }

    private List<File> getListFiles(File parentDir) {
        Logger.e(TAG, parentDir.getAbsolutePath());
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".png")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public List<Attachment> getScreenshotList() {
        setScreenshotList();
        if (screenshotList == null) {
            screenshotList = new ArrayList<>();}
            if (screenArray != null) {
                for (int i = 0; i < screenArray.size(); i++) {
                    Attachment screenshot = new Attachment();
                    screenshot.name = screenArray.get(i).replaceAll(screenPath + "/Pictures/Screenshots/", "/");
                    screenshot.imageName = screenArray.get(i);
                    screenshotList.add(screenshot);
                }
            }
        return screenshotList;
    }

}
