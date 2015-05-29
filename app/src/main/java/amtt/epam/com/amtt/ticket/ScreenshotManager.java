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

    public List<Attachment> getScreenshotList() {
        ArrayList<String> screenArray = AmttFileObserver.getImageArray();
        screenshotList.clear();
        if (screenArray != null) {
            for (int i = 0; i < screenArray.size(); i++) {
                Attachment screenshot = new Attachment();
                screenshot.name = screenArray.get(i).replaceAll("/Pictures/Screenshots/", "/");
                screenshot.imageName = screenArray.get(i);
                screenshotList.add(screenshot);
            }
        }
        return screenshotList;
    }

}
