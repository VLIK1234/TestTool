package amtt.epam.com.amtt.database.dao;

import android.content.ContentValues;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public class UserDao implements DaoInterface<JiraUserInfo> {

    @Override
    public int add(JiraUserInfo user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersTable._USER_NAME, user.getName());
        contentValues.put(UsersTable._DISPLAY_NAME, user.getDisplayName());
        contentValues.put(UsersTable._TIME_ZONE, user.getTimeZone());
        contentValues.put(UsersTable._LOCALE, user.getLocale());
        contentValues.put(UsersTable._URL, user.getUrl());
        contentValues.put(UsersTable._KEY, user.getKey());
        contentValues.put(UsersTable._EMAIL, user.getEmailAddress());
        contentValues.put(UsersTable._AVATAR_16, user.getAvatarUrls().getAvatarXSmallUrl());
        contentValues.put(UsersTable._AVATAR_24, user.getAvatarUrls().getAvatarSmallUrl());
        contentValues.put(UsersTable._AVATAR_32, user.getAvatarUrls().getAvatarMediumUrl());
        contentValues.put(UsersTable._AVATAR_48, user.getAvatarUrls().getAvatarUrl());
        Uri insertedUserUri = ContextHolder.getContext().getContentResolver().insert(AmttContentProvider.USER_CONTENT_URI, contentValues);
        return Integer.valueOf(insertedUserUri.getLastPathSegment());
    }

    @Override
    public void remove(JiraUserInfo object) {
        /*
        * will be added as necessary
        * */
    }

    @Override
    public void removeByKey(int key) {
        /*
        * will be added as necessary
        * */
    }

    @Override
    public void removeAll() throws Exception {
        ContextHolder.getContext().getContentResolver().delete(AmttContentProvider.USER_CONTENT_URI, null, null);
    }

    @Override
    public void update(JiraUserInfo object) {
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

        ContextHolder.getContext().getContentResolver().update(AmttContentProvider.USER_CONTENT_URI,
                contentValues,
                UsersTable._ID + "=?",
                new String[]{ String.valueOf(object.getId()) });
    }

    @Override
    public List<JiraUserInfo> getAll() {
        /*
        * will be added as necessary
        * */
        return null;
    }

    @Override
    public JiraUserInfo getByKey(int key) {
        /*
        * will be added as necessary
        * */
        return null;
    }

}
