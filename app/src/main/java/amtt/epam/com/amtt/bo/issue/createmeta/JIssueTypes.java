package amtt.epam.com.amtt.bo.issue.createmeta;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */

import com.google.gson.annotations.SerializedName;

/**
 * JiraIssueTypes = JIssueTypes
 * <p/>
 * request URL : .../issue/createmeta ...
 * <p/>
 * response json : {...
 * projects{
 * ...
 * issuetypes{
 * }
 * }
 * }
 */
public class JIssueTypes {

    @SerializedName("self")
    private String mSelf;
    @SerializedName("id")
    private int mId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("subtask")
    private Boolean mSubtask;
    @SerializedName("expand")
    private String mExpand;
    @SerializedName("fields")
    private JITFields mFields;

    public JIssueTypes() {
    }

    public JIssueTypes(String self, int id, String description, String iconUrl, String name, Boolean subtask, String expand, JITFields fields) {
        this.mSelf = self;
        this.mId = id;
        this.mDescription = description;
        this.mIconUrl = iconUrl;
        this.mName = name;
        this.mSubtask = subtask;
        this.mExpand = expand;
        this.mFields = fields;
    }

    public JIssueTypes(String self, int id, String name, String iconUrl) {
        this.mSelf = self;
        this.mId = id;
        this.mName = name;
        this.mIconUrl = iconUrl;
    }

    public JIssueTypes(String mSelf, int mId, String mDescription, String mIconUrl, String mName, Boolean mSubtask) {
        this.mSelf = mSelf;
        this.mId = mId;
        this.mDescription = mDescription;
        this.mIconUrl = mIconUrl;
        this.mName = mName;
        this.mSubtask = mSubtask;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
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

    public Boolean getSubtask() {
        return mSubtask;
    }

    public void setSubtask(Boolean subtask) {
        this.mSubtask = subtask;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }

    public JITFields getFields() {
        return mFields;
    }

    public void setFields(JITFields fields) {
        this.mFields = fields;
    }
}
