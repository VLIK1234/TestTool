package amtt.epam.com.amtt.bo.issue.createmeta;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.createmeta.issuetypes.JFields;

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
    private String mId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("subtask")
    private Boolean mSubTask;
    @SerializedName("expand")
    private String mExpand;
    @SerializedName("fields")
    private JFields mFields;

    public JIssueTypes() {
    }

    public JIssueTypes(String self, String id, String description, String iconUrl, String name, Boolean subTask, String expand, JFields fields) {
        this.mSelf = self;
        this.mId = id;
        this.mDescription = description;
        this.mIconUrl = iconUrl;
        this.mName = name;
        this.mSubTask = subTask;
        this.mExpand = expand;
        this.mFields = fields;
    }

    public JIssueTypes(String self, String id, String name, String iconUrl) {
        this.mSelf = self;
        this.mId = id;
        this.mName = name;
        this.mIconUrl = iconUrl;
    }

    public JIssueTypes(String mSelf, String mId, String mDescription, String mIconUrl, String mName, Boolean mSubTask) {
        this.mSelf = mSelf;
        this.mId = mId;
        this.mDescription = mDescription;
        this.mIconUrl = mIconUrl;
        this.mName = mName;
        this.mSubTask = mSubTask;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
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

    public Boolean getSubTask() {
        return mSubTask;
    }

    public void setSubTask(Boolean subTask) {
        this.mSubTask = subTask;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }

    public JFields getFields() {
        return mFields;
    }

    public void setFields(JFields fields) {
        this.mFields = fields;
    }
}
