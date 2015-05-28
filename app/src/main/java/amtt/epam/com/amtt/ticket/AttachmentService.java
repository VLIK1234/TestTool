package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.util.Logger;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;

/**
 @author Iryna Monchanka
 @version on 27.05.2015
 */

public class AttachmentService extends Service {

    public static final String ACTION_START = "SHOW";
    public static final String ACTION_CLOSE = "CLOSE";
    public static final String RESULT = "RESULT";
    private static final String TAG = "Log";
    private static Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        context = getBaseContext();
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
                    attachFile(result, AmttFileObserver.getImageArray());
                }
            }
        });
    }

    public static void start(Context context) {
        context.startService(new Intent(context, AttachmentService.class).setAction(ACTION_START));
    }

    public void attachFile(String issueKey, ArrayList<String> fileFullName) {
        JiraContent.getInstance().sendAttachment(issueKey, fileFullName, new JiraGetContentCallback<Boolean>() {
            @Override
            public void resultOfDataLoading(Boolean result) {
                sendActionScreenshot(result);
            }
        });
    }

    public static void sendActionScreenshot(Boolean result){
        Intent intent = new Intent(context, AttachmentService.class).setAction(ACTION_CLOSE);
        intent.putExtra(RESULT, result);
        context.startService(intent);
    }
}
