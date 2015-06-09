package amtt.epam.com.amtt.ticket;

import java.util.ArrayList;
import java.util.List;

/**
@author Iryna Monchanka
@version on 27.05.2015
 */

public class ScreenshotManager {

    private static ScreenshotManager mInstance;
    private ArrayList<Attachment> screenshotList = new ArrayList<>();

    private ScreenshotManager(){}

    public static ScreenshotManager getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenshotManager();
        }
        return mInstance;
    }

    public ArrayList<Attachment> getScreenshotList(ArrayList<String> listScreenshot) {
        screenshotList.clear();
        if (listScreenshot != null) {
            for (int i = 0; i < listScreenshot.size(); i++) {
                Attachment screenshot = new Attachment();
                screenshot.name = listScreenshot.get(i).replaceAll("/Pictures/Screenshots/", "/");
                screenshot.filePath = listScreenshot.get(i);
                screenshotList.add(screenshot);
            }
        }
        return screenshotList;
    }

}