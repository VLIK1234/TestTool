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
    public static int numberNotification = 1;
    public static int notificationId = 1;

    public static void showNotification(Context context, NotificationCompat.Builder builder){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(getNotificationId(), builder.build());
    }

    public static void updateNotification(Context context, NotificationCompat.Builder builder, int notificationId){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }

    public static NotificationCompat.Builder getInitBuilder(Context context, int countAttachItem){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_file_upload)
                .setContentTitle(context.getString(R.string.notification_attach_title_init))
                        .setContentText(String.format(context.getString(R.string.notification_attachment_text_init), countAttachItem))
                        .setTicker(context.getString(R.string.notification_attach_title_init))
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setNumber(numberNotification)
                        .setGroup(NOTIFICATION_ATTACH_GROUP)
                        .setProgress(100, 100, true);
        numberNotification+=1;
        return builder;
    }

    public static NotificationCompat.Builder getFinalBuilder(Context context, int countAttachItem){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_action_done)
                .setContentTitle(context.getString(R.string.notification_attach_title_done))
                .setContentText(String.format(context.getString(R.string.notification_attachment_text_done), countAttachItem))
                .setTicker(context.getString(R.string.notification_attach_title_done))
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setNumber(numberNotification)
                .setGroup(NOTIFICATION_ATTACH_GROUP)
                .setProgress(100, 100, false);
        return builder;
    }

    public static int getNotificationId() {
        if (notificationId <= 5) {
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
