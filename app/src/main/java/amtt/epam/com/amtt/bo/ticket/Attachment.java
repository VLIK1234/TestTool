package amtt.epam.com.amtt.bo.ticket;

import amtt.epam.com.amtt.bo.ticket.Step.ScreenshotState;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class Attachment {


    private final int mStepId;
    private final String mFileName;
    private final String mFilePath;
    private final ScreenshotState mScreenshotState;

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
    public int getStepId() {
        return mStepId;
    }

    public String getFileName() {
        return mFileName;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public ScreenshotState getScreenshotState() {
        return mScreenshotState;
    }
}
