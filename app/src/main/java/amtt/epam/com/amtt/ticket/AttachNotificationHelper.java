package amtt.epam.com.amtt.ticket;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 29.05.2015.
 */
public class AttachNotificationHelper {
    public static final String NOTIFICATION_ATTACH_GROUP = "Attach";
    public static int notificationId = 1;

    public static int showNotification(Context context, NotificationCompat.Builder builder){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(getNotificationId(), builder.build());
        return getCurrentNotificationId();
    }

    public static void updateNotification(Context context, NotificationCompat.Builder builder, int notificationId){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }

    public static NotificationCompat.Builder getInitBuilder(Context context, String issueKey, int countAttachItem){
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_file_upload)
                .setContentTitle(String.format(context.getString(R.string.notification_attach_title_init), issueKey))
                .setContentText(String.format(context.getString(R.string.notification_attachment_text_init), countAttachItem))
                .setTicker(String.format(context.getString(R.string.notification_attach_title_init), issueKey))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(NOTIFICATION_ATTACH_GROUP)
                .setProgress(100, 100, true);
    }

    public static NotificationCompat.Builder getFinalBuilder(Context context, String issueKey, int countAttachItem){
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_action_done)
                .setContentTitle(String.format(context.getString(R.string.notification_attach_title_done), issueKey))
                .setContentText(String.format(context.getString(R.string.notification_attachment_text_done), countAttachItem))
                .setTicker(String.format(context.getString(R.string.notification_attach_title_done), issueKey))
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(NOTIFICATION_ATTACH_GROUP)
                .setProgress(100, 100, false);
    }

    public static int getNotificationId() {
        if (notificationId < 5) {
            notificationId++;
            return notificationId;
        }else{
            notificationId = 1;
            return notificationId;
        }
    }

    public static int getCurrentNotificationId(){
        return notificationId;
    }
}
