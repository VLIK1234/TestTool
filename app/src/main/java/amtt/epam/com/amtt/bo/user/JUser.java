package amtt.epam.com.amtt.bo.user;

import android.database.Cursor;

import amtt.epam.com.amtt.bo.JBaseObject;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 30.03.2015
 */

public class JUser<T> extends JBaseObject {

    @SerializedName("emailAddress")
    protected String mEmailAddress;
    @SerializedName("displayName")
    protected String mDisplayName;
    @SerializedName("active")
    private Boolean mActive;
    @SerializedName("timeZone")
    protected String mTimeZone;

    public JUser(){}

    @Override
    public T parse(Cursor cursor) {
        return null;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public Boolean isActive() {
        return mActive;
    }

    public void setActive(Boolean active) {
        this.mActive = active;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        this.mTimeZone = timeZone;
    }

}
