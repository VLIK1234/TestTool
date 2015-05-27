package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.observer.AmttFileObserver;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
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
                    JiraContent.getInstance().getRecentIssueKey(new JiraGetContentCallback<String>() {
                        @Override
                        public void resultOfDataLoading(String result) {
                            if (result != null) {
                                attachFile(result, AmttFileObserver.getImageArray());
                            }
                        }
                    });
                    break;
                case ACTION_CLOSE:
                    Bundle extra = intent.getExtras();
                    if (extra!=null) {
                       if(extra.getBoolean(RESULT)){
                           stopSelf();
                       }
                    }
                    break;
            }
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {

    }

    public static void start(Context context) {
        context.startService(new Intent(context, AttachmentService.class).setAction(ACTION_START));
    }

    public static void close(Context context) {
        context.startService(new Intent(context, AttachmentService.class).setAction(ACTION_CLOSE));
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
