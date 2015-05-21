package amtt.epam.com.amtt.bo.project;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JPriority {

    @SerializedName("self")
    private String mSelf;
    @SerializedName("statusColor")
    private String mStatusColor;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("id")
    private String mId;

    public JPriority(){}

    public JPriority(String self, String statusColor, String description, String iconUrl, String name, String id) {
        this.mSelf = self;
        this.mStatusColor = statusColor;
        this.mDescription = description;
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

    public String getStatusColor() {
        return mStatusColor;
    }

    public void setStatusColor(String statusColor) {
        this.mStatusColor = statusColor;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
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
