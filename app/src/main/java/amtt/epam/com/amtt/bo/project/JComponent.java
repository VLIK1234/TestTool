package amtt.epam.com.amtt.bo.project;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.user.JUser;

/**
 @author Iryna Monchanka
 @version on 4/23/2015
 */

public class JComponent {

    @SerializedName("id")
    private String mId;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("lead")
    private JUser mLead;
    @SerializedName("assigneeType")
    private String mAssigneeType;
    @SerializedName("assignee")
    private JUser mAssignee;
    @SerializedName("realAssigneeType")
    private String mRealAssigneeType;
    @SerializedName("realAssignee")
    private JUser mRealAssignee;
    @SerializedName("isAssigneeTypeValid")
    private Boolean mIsAssigneeTypeValid;
    @SerializedName("project")
    private String mProjectKey;
    @SerializedName("projectId")
    private String mProjectId;

    public JComponent(){}

    public JComponent(String id) {
        this.mId = id;
    }

    public JComponent(String id, String self, String name, String description,
                      Boolean isAssigneeTypeValid) {
        this.mId = id;
        this.mSelf = self;
        this.mName = name;
        this.mDescription = description;
        this.mIsAssigneeTypeValid = isAssigneeTypeValid;
    }

    public JComponent(String mId, String mSelf, String mName, String mDescription, JUser mLead,
                      String mAssigneeType, JUser mAssignee, String mRealAssigneeType,
                      JUser mRealAssignee, Boolean mIsAssigneeTypeValid, String mProjectKey,
                      String mProjectId) {
        this.mId = mId;
        this.mSelf = mSelf;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mLead = mLead;
        this.mAssigneeType = mAssigneeType;
        this.mAssignee = mAssignee;
        this.mRealAssigneeType = mRealAssigneeType;
        this.mRealAssignee = mRealAssignee;
        this.mIsAssigneeTypeValid = mIsAssigneeTypeValid;
        this.mProjectKey = mProjectKey;
        this.mProjectId = mProjectId;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Boolean isAssigneeTypeValid() {
        return mIsAssigneeTypeValid;
    }

    public void setIsAssigneeTypeValid(Boolean isAssigneeTypeValid) {
        this.mIsAssigneeTypeValid = isAssigneeTypeValid;
    }

    public JUser getLead() {
        return mLead;
    }

    public void setLead(JUser lead) {
        this.mLead = lead;
    }

    public String getAssigneeType() {
        return mAssigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.mAssigneeType = assigneeType;
    }

    public JUser getAssignee() {
        return mAssignee;
    }

    public void setAssignee(JUser assignee) {
        this.mAssignee = assignee;
    }

    public String getRealAssigneeType() {
        return mRealAssigneeType;
    }

    public void setRealAssigneeType(String realAssigneeType) {
        this.mRealAssigneeType = realAssigneeType;
    }

    public JUser getRealAssignee() {
        return mRealAssignee;
    }

    public void setRealAssignee(JUser realAssignee) {
        this.mRealAssignee = realAssignee;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        this.mProjectId = projectId;
    }

    public String getProjectKey() {
        return mProjectKey;
    }

    public void setProjectKey(String projectKey) {
        this.mProjectKey = projectKey;
    }
}
