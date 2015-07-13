package amtt.epam.com.amtt.bo.ticket;

import amtt.epam.com.amtt.bo.database.Step.ScreenshotState;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class Attachment {

    public String mStepId;
    public String mName;
    public String mFilePath;
    public ScreenshotState mScreenshotState;

    public Attachment(int stepId, String name, String filePath, ScreenshotState screenshotState) {
        mStepId = String.valueOf(stepId);
        mName = name;
        mFilePath = filePath;
        mScreenshotState = screenshotState;
    }

}
