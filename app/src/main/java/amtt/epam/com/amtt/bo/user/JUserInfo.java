package amtt.epam.com.amtt.bo.user;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JAvatarUrls;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JUserInfo extends JUser {

    @SerializedName("locale")
    private String mLocale;
    @SerializedName("groups")
    private JUserGroup mGroups;
    @SerializedName("expand")
    private String mExpand;

    public JUserInfo() {
    }

    public JUserInfo(String key, String self, String name, JAvatarUrls avatarUrls, String emailAddress, String displayName, Boolean active, String timeZone, String locale, JUserGroup groups, String expand) {
       super(key, self, name, avatarUrls, emailAddress, displayName, active, timeZone);
        this.mLocale = locale;
        this.mGroups = groups;
        this.mExpand = expand;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public JUserGroup getGroups() {
        return mGroups;
    }

    public void setGroups(JUserGroup groups) {
        this.mGroups = groups;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }
}
