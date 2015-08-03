package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 3/26/2015
 */

public class JIssueTypesIssueType {

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


    public JIssueTypesIssueType() {
    }

    public JIssueTypesIssueType(String id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public JIssueTypesIssueType(String id, String name, String self, String description, String iconUrl, Boolean subtask) {
        this.mId = id;
        this.mName = name;
        this.mSelf = self;
        this.mDescription = description;
        this.mIconUrl = iconUrl;
        this.mSubtask = subtask;
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
