package amtt.epam.com.amtt.bo.ticket;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.database.Step.ScreenshotState;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class Attachment {

    public int mStepId;
    public String mFileName;
    public String mFilePath;
    public ScreenshotState mScreenshotState;

    public Attachment(Step step) {
        mStepId = step.getId();
        mFilePath = step.getScreenshotPath();
        mFileName = FileUtil.getFileName(mFilePath);
        mScreenshotState = step.getScreenshotState();
    }

    public Attachment(String filePath) {
        mStepId = -1;
        mFilePath = filePath;
        mFileName = FileUtil.getFileName(mFilePath);
        mScreenshotState = ScreenshotState.WRITTEN;
    }

}
