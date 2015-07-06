package amtt.epam.com.amtt.bo.ticket;

import amtt.epam.com.amtt.bo.database.Step.ScreenshotState;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class Attachment {

    public String mName;
    public String mFilePath;
    public ScreenshotState mStepScreenshotState;

    public Attachment(String name, String filePath, ScreenshotState screenshotState) {
        mName = name;
        mFilePath = filePath;
        mStepScreenshotState = screenshotState;
    }

}
