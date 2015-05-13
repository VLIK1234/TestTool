package amtt.epam.com.amtt.database.dao;

import android.content.ContentValues;
import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public class UserDao extends AbstractDao<JiraUserInfo> {

    public UserDao() {
        mUris = new Uri[] { AmttUri.USER.get() };
    }

    @Override
    List<ContentValues> getAddValues(JiraUserInfo object) throws Exception {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(UsersTable._USER_NAME, object.getName());
        contentValues.put(UsersTable._DISPLAY_NAME, object.getDisplayName());
        contentValues.put(UsersTable._TIME_ZONE, object.getTimeZone());
        contentValues.put(UsersTable._LOCALE, object.getLocale());
        contentValues.put(UsersTable._URL, object.getUrl());
        contentValues.put(UsersTable._KEY, object.getKey());
        contentValues.put(UsersTable._EMAIL, object.getEmailAddress());
        contentValues.put(UsersTable._AVATAR_16, object.getAvatarUrls().getAvatarXSmallUrl());
        contentValues.put(UsersTable._AVATAR_24, object.getAvatarUrls().getAvatarSmallUrl());
        contentValues.put(UsersTable._AVATAR_32, object.getAvatarUrls().getAvatarMediumUrl());
        contentValues.put(UsersTable._AVATAR_48, object.getAvatarUrls().getAvatarUrl());
        return new ArrayList<ContentValues>() {{ add(contentValues); }};
    }

    @Override
    ContentValues getUpdateValues(JiraUserInfo object) throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersTable._DISPLAY_NAME, object.getDisplayName());
        contentValues.put(UsersTable._TIME_ZONE, object.getTimeZone());
        contentValues.put(UsersTable._LOCALE, object.getLocale());
        contentValues.put(UsersTable._KEY, object.getKey());
        contentValues.put(UsersTable._EMAIL, object.getEmailAddress());
        contentValues.put(UsersTable._AVATAR_16, object.getAvatarUrls().getAvatarXSmallUrl());
        contentValues.put(UsersTable._AVATAR_24, object.getAvatarUrls().getAvatarSmallUrl());
        contentValues.put(UsersTable._AVATAR_32, object.getAvatarUrls().getAvatarMediumUrl());
        contentValues.put(UsersTable._AVATAR_48, object.getAvatarUrls().getAvatarUrl());
        return contentValues;
    }

    @Override
    void removeAllExtra() throws Exception {

    }

    @Override
    void addExtra(JiraUserInfo object) throws Exception {

    }

}
