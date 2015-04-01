package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraIssueTypesIssueType {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("subtask")
    private Boolean mSubtask;


    public JiraIssueTypesIssueType() {
    }

    public JiraIssueTypesIssueType(String id, String name) {
        this.mId = id;
        this.mName = name;
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

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
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

    public Boolean getSubtask() {
        return mSubtask;
    }

    public void setSubtask(Boolean subtask) {
        this.mSubtask = subtask;
    }
}
