package amtt.epam.com.amtt.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.helper.AttachNotificationHelper;
import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.TestUtil;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class AttachmentService extends Service {

    public static final String ACTION_START = "START";
    public static final String ACTION_CLOSE = "CLOSE";
    public static final String RESULT = "RESULT";
    private static final String TAG = "Log";
    private static List<String> attachmentList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_START:
                    checkIssueKey();
                    Logger.d(TAG, ACTION_START);
                    break;
                case ACTION_CLOSE:
                    Bundle extra = intent.getExtras();
                    if (extra != null) {
                        if (extra.getBoolean(RESULT)) {
                            stopSelf();
                            Logger.d(TAG, String.valueOf(extra.getBoolean(RESULT)));
                        }
                    }
                    Logger.d(TAG, ACTION_CLOSE);
                    break;
            }
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void checkIssueKey() {
        JiraContent.getInstance().getRecentIssueKey(new JiraGetContentCallback<String>() {
            @Override
            public void resultOfDataLoading(String result) {
                if (result != null) {
                    attachFile(result, attachmentList);
                }
            }
        });
    }

    public static void start(Context context, ArrayList<String> attachmentList) {
        AttachmentService.attachmentList = (ArrayList<String>) attachmentList.clone();
        context.startService(new Intent(context, AttachmentService.class).setAction(ACTION_START));
    }

    public void attachFile(final String issueKey, final List<String> fileFullName) {
        if (fileFullName.size() > 0) {
            final int notificationId = AttachNotificationHelper.showNotification(getBaseContext(),
                    AttachNotificationHelper.getInitBuilder(getBaseContext(), issueKey, fileFullName.size()));
            JiraContent.getInstance().sendAttachment(issueKey, fileFullName, new JiraGetContentCallback<Boolean>() {
                @Override
                public void resultOfDataLoading(Boolean result) {
                    AttachNotificationHelper.updateNotification(getBaseContext(),
                            AttachNotificationHelper.getFinalBuilder(getBaseContext(), issueKey, fileFullName.size()), notificationId);
                    TestUtil.closeTest();
                    AmttFileObserver.clearImageArray();
                    stopSelf();
                }
            });
        }
    }
}
