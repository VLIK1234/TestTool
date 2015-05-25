package amtt.epam.com.amtt.database.object;

import android.database.Cursor;

import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;

/**
 * Created by Ivan_Bakach on 25.05.2015.
 */
public class DbEntityFactory {

    public static DatabaseEntity createEntity(DbEntityType entityType, Cursor cursor) {
        switch (entityType) {
            case STEP:
                return new Step(cursor);
            case ACTIVITY_META:
                return new ActivityMeta(cursor);
            case JUSER_INFO:
                return new JUserInfo(cursor);
            default:
                return null;
        }
    }
    public static DbEntityType getTypeEntityEnum(DatabaseEntity entity){
        if (entity instanceof Step) {
            return DbEntityType.STEP;
        }else if (entity instanceof ActivityMeta) {
            return DbEntityType.ACTIVITY_META;
        }else if (entity instanceof JUserInfo) {
            return DbEntityType.JUSER_INFO;
        }else {
            return null;
        }
    }
}
