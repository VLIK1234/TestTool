package amtt.epam.com.amtt.bo.ticket;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.bo.database.Step.ScreenshotState;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class Attachment {

    public int mStepId;
    public boolean isStepWithActivityInfo;
    public String mFileName;
    public String mFilePath;
    public ScreenshotState mScreenshotState;

    public Attachment(Step step) {
        mStepId = step.getId();
        isStepWithActivityInfo = step.isStepWithActivityInfo();
        mFilePath = step.getScreenshotPath();
        mFileName = FileUtil.getFileName(mFilePath);
        mScreenshotState = step.getScreenshotState();
    }

    public Attachment(String fileName, String filePath) {
        mFileName = fileName;
        mFilePath = filePath;
        mScreenshotState = ScreenshotState.WRITTEN;
    }

}
