package amtt.epam.com.amtt.bo.issue.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JiraUserInfo extends DatabaseEntity {

    @SerializedName("key")
    private String mKey;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JiraAvatarUrls mAvatarUrls;
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

    public JiraUserInfo() { }

    public JiraUserInfo(Cursor cursor) {
        cursor.moveToFirst();
        mEmailAddress = cursor.getString(cursor.getColumnIndex(UsersTable._EMAIL));
        mDisplayName= cursor.getString(cursor.getColumnIndex(UsersTable._DISPLAY_NAME));
        mTimeZone= cursor.getString(cursor.getColumnIndex(UsersTable._TIME_ZONE));
        mLocale= cursor.getString(cursor.getColumnIndex(UsersTable._LOCALE));
        String avatar16 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_16));
        String avatar24 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_24));
        String avatar32 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_32));
        String avatar48 = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_48));
        mAvatarUrls = new JiraAvatarUrls(avatar48, avatar24, avatar16, avatar32);
    }

    public JiraUserInfo(String key, String self, String name, JiraAvatarUrls avatarUrls, String emailAddress, String displayName, String timeZone, String locale) {
        this.mKey = key;
        this.mSelf = self;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mEmailAddress = emailAddress;
        this.mDisplayName = displayName;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
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

    public JiraAvatarUrls getAvatarUrls() {
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
        return values;
    }

}
