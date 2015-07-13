package amtt.epam.com.amtt.util;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.database.object.DatabaseEntity;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class AttachmentManager {

    private static AttachmentManager mInstance;
    private ArrayList<Attachment> attachmentArrayList = new ArrayList<>();

    private AttachmentManager() {
    }

    public static AttachmentManager getInstance() {
        if (mInstance == null) {
            mInstance = new AttachmentManager();
        }
        return mInstance;
    }

    public ArrayList<Attachment> getAttachmentList(List<DatabaseEntity> result) {
        attachmentArrayList.clear();
        if (result != null) {
            ArrayList<Step> listStep = (ArrayList) result;
            for (Step step : listStep) {
                if (step.getScreenshotPath() != null) {
                    attachmentArrayList.add(new Attachment(step.getId(), FileUtil.getFileName(step.getScreenshotPath()), step.getScreenshotPath(), step.getScreenshotState()));
                }
            }
        }

        return attachmentArrayList;
    }
}
