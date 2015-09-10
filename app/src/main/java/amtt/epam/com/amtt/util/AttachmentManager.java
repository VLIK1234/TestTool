package amtt.epam.com.amtt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.database.object.DatabaseEntity;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class AttachmentManager {

    private static AttachmentManager mInstance;
    private final ArrayList<Attachment> attachmentArrayList = new ArrayList<>();

    private AttachmentManager() {
    }

    public static AttachmentManager getInstance() {
        if (mInstance == null) {
            mInstance = new AttachmentManager();
        }
        return mInstance;
    }

    public <Entity extends DatabaseEntity>ArrayList<Attachment> getAttachmentList(List<Entity> result) {
        attachmentArrayList.clear();
        if (result != null) {
            ArrayList<Step> listStep = (ArrayList<Step>) result;
            for (Step step : listStep) {
                if (step.getScreenshotPath() != null) {
                    attachmentArrayList.add(new Attachment(step));
                }
            }
        }
        return attachmentArrayList;
    }

    public List<Attachment> stepsToAttachments(List<Step> result){
        List<Attachment> screenArray;
        if (result != null) {
            screenArray = getAttachmentList(result);
        } else {
            screenArray = new ArrayList<>();
        }
        File externalCache = new File(FileUtil.getCacheLocalDir());
        String template = externalCache.getPath() + "/%s";
        String pathLogCommon = String.format(template, "log_common.txt");
        String pathLogException = String.format(template, "log_exception.txt");
        String pathLogArguments = String.format(template, "log_arguments.txt");
        final File fileLogCommon = new File(pathLogCommon);
        final File fileLogException = new File(pathLogException);
        final File fileLogArguments = new File(pathLogArguments);
        final Attachment attachLogCommon = new Attachment(pathLogCommon);
        final Attachment attachLogException = new Attachment(pathLogException);
        final Attachment attachLogArguments = new Attachment(pathLogArguments);
        if (PreferenceUtil.getBoolean("IS_ATTACH_LOGS")) {
            if (fileLogCommon.exists() && fileLogException.exists()) {
                screenArray.add(attachLogCommon);
                screenArray.add(attachLogException);
                if (fileLogArguments.exists()) {
                    screenArray.add(attachLogArguments);
                }
            }
        }
        return screenArray;
    }
}
