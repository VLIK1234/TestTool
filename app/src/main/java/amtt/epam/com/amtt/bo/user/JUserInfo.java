package amtt.epam.com.amtt.bo.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JAvatarUrls;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public class JUserInfo extends DatabaseEntity<JUserInfo> {

    @SerializedName("key")
    private String mKey;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JAvatarUrls mAvatarUrls;
    @SerializedName("emailAddress")
    private String mEmailAddress;
    @SerializedName("displayName")
    private String mDisplayName;
    @SerializedName("timeZone")
    private String mTimeZone;
    @SerializedName("locale")
    private String mLocale;

    private String mUrl;
    private int mId;
    private String mCredentials;
    private String mLastProjectKey;

    public JUserInfo() {
    }

    public JUserInfo(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(UsersTable._ID));
        mName = cursor.getString(cursor.getColumnIndex(UsersTable._USER_NAME));
        mUrl = cursor.getString(cursor.getColumnIndex(UsersTable._URL));
        mEmailAddress = cursor.getString(cursor.getColumnIndex(UsersTable._EMAIL));
        mDisplayName = cursor.getString(cursor.getColumnIndex(UsersTable._DISPLAY_NAME));
        mTimeZone = cursor.getString(cursor.getColumnIndex(UsersTable._TIME_ZONE));
        mLocale = cursor.getString(cursor.getColumnIndex(UsersTable._LOCALE));
        String avatar16 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_16));
        String avatar24 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_24));
        String avatar32 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_32));
        String avatar48 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_48));
        mCredentials = cursor.getString(cursor.getColumnIndex(UsersTable._CREDENTIALS));
        mAvatarUrls = new JAvatarUrls(avatar48, avatar24, avatar16, avatar32);
        mLastProjectKey = cursor.getString(cursor.getColumnIndex(UsersTable._LAST_PROJECT_KEY));
    }

    public JUserInfo(String key, String self, String name, JAvatarUrls avatarUrls, String emailAddress, String displayName, String timeZone, String locale) {
        this.mKey = key;
        this.mSelf = self;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mEmailAddress = emailAddress;
        this.mDisplayName = displayName;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
    }

    @Override
    public JUserInfo parse(Cursor cursor) {
        return new JUserInfo(cursor);
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public JAvatarUrls getAvatarUrls() {
        return mAvatarUrls;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public String getLocale() {
        return mLocale;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getCredentials() {
        return mCredentials;
    }

    public void setCredentials(String credentials) {
        this.mCredentials = credentials;
    }

    public String getLastProjectKey() {
        return mLastProjectKey;
    }

    public void setLastProjectKey(String lastProjectKey) {
        this.mLastProjectKey = lastProjectKey;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return AmttUri.USER.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UsersTable._USER_NAME, mName);
        values.put(UsersTable._DISPLAY_NAME, mDisplayName);
        values.put(UsersTable._TIME_ZONE, mTimeZone);
        values.put(UsersTable._LOCALE, mLocale);
        values.put(UsersTable._URL, mUrl);
        values.put(UsersTable._KEY, mKey);
        values.put(UsersTable._EMAIL, mEmailAddress);
        values.put(UsersTable._AVATAR_16, getAvatarUrls().getAvatarXSmallUrl());
        values.put(UsersTable._AVATAR_24, getAvatarUrls().getAvatarSmallUrl());
        values.put(UsersTable._AVATAR_32, getAvatarUrls().getAvatarMediumUrl());
        values.put(UsersTable._AVATAR_48, getAvatarUrls().getAvatarUrl());
        values.put(UsersTable._CREDENTIALS, mCredentials);
        values.put(UsersTable._LAST_PROJECT_KEY, mLastProjectKey);
        return values;
    }

}
