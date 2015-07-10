package amtt.epam.com.amtt.bo.ticket;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class Attachment {

    public int mStepId;
    public boolean isStepWithActivityInfo;
    public String mFileName;
    public String mFilePath;

    public Attachment(Step step) {
        mStepId = step.getId();
        isStepWithActivityInfo = step.isStepWithActivityInfo();
        mFileName = FileUtil.getFileName(step.getFilePath());
        mFilePath = step.getFilePath();
    }

}
