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
@author Iryna Monchanka
@version on 27.05.2015
 */

public class AttachmentManager {

    private static AttachmentManager mInstance;
    private ArrayList<Attachment> attachmentArrayList = new ArrayList<>();

    private AttachmentManager(){}

    public static AttachmentManager getInstance() {
        if (mInstance == null) {
            mInstance = new AttachmentManager();
        }
        return mInstance;
    }

    public ArrayList<Attachment> getAttachmentList(ArrayList<String> listScreenshot) {
        attachmentArrayList.clear();
        if (listScreenshot != null) {
            for (int i = 0; i < listScreenshot.size(); i++) {
                Attachment screenshot = new Attachment(FileUtil.getFileName(listScreenshot.get(i)),listScreenshot.get(i));
                attachmentArrayList.add(screenshot);
            }
        }
        return attachmentArrayList;
    }

    public ArrayList<Attachment> getAttachmentList(List<DatabaseEntity> result) {
        attachmentArrayList.clear();
        if (result!=null) {
            ArrayList<Step> listStep = (ArrayList) result;
            for (Step step:listStep) {
                if (step.getFilePath()!=null) {
                    attachmentArrayList.add(new Attachment(FileUtil.getFileName(step.getFilePath()), step.getFilePath()));
                }
            }
            if (PreferenceUtils.getBoolean(AmttApplication.getContext().getString(R.string.key_is_attach_logs))) {

                String template = Environment.getExternalStorageDirectory()+"/Android"+Environment.getDataDirectory()+
                        "/%s"+Environment.getDownloadCacheDirectory()+"/%s";
                String pathLogCommon = String.format(template, PreferenceUtils.getString(AmttApplication.getContext().getString(R.string.key_test_project)),"log_common.txt");
                String pathLogException = String.format(template, PreferenceUtils.getString(AmttApplication.getContext().getString(R.string.key_test_project)),"log_exception.txt");
                File fileLogCommon = new File(pathLogCommon);
                File fileLogException = new File(pathLogException);
                if (fileLogCommon.exists()&&fileLogException.exists()) {
                    attachmentArrayList.add(new Attachment(FileUtil.getFileName(pathLogCommon),pathLogCommon));
                    attachmentArrayList.add(new Attachment(FileUtil.getFileName(pathLogException),pathLogException));
                }
            }
        }

        return attachmentArrayList;
    }
}
