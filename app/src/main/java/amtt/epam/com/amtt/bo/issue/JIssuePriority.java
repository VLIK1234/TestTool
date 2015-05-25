package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public class JIssuePriority {

    @SerializedName("self")
    private String mSelf;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("id")
    private String mId;

    public JIssuePriority(){}

    public JIssuePriority(String self, String iconUrl, String name, String id) {
        this.mSelf = self;
        this.mIconUrl = iconUrl;
        this.mName = name;
        this.mId = id;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
