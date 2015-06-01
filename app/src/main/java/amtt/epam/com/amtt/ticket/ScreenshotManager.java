package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.observer.AmttFileObserver;
import java.util.ArrayList;
import java.util.List;

/**
@author Iryna Monchanka
@version on 27.05.2015
 */

public class ScreenshotManager {

    private static ScreenshotManager mInstance;
    private List<Attachment> screenshotList = new ArrayList<>();

    public static ScreenshotManager getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenshotManager();
        }
        return mInstance;
    }

    public List<Attachment> getScreenshotList(ArrayList<String> listScreenshot) {
        screenshotList.clear();
        if (listScreenshot != null) {
            for (int i = 0; i < listScreenshot.size(); i++) {
                Attachment screenshot = new Attachment();
                screenshot.name = listScreenshot.get(i).replaceAll("/Pictures/Screenshots/", "/");
                screenshot.imageName = listScreenshot.get(i);
                screenshotList.add(screenshot);
            }
        }
        return screenshotList;
    }

}
