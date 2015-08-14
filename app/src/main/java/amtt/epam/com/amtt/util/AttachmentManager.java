package amtt.epam.com.amtt.util;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.bo.database.Step;
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
}
