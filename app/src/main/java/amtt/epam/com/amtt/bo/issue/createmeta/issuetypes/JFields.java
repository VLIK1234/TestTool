package amtt.epam.com.amtt.bo.issue.createmeta.issuetypes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */

/**
 * JiraIssueTypesFields = JFields
 * <p/>
 * request URL : .../issue/createmeta ...
 * <p/>
 * response json : {...
 * projects{
 * ...
 * issuetypes{
 * ...
 * fields{
 * }
 * }
 * }
 * }
 */

public class JFields {

    @SerializedName("summary")
    private JSummary mSummary;
    @SerializedName("attachment")
    private JAttachment mAttachment;
    @SerializedName("description")
    private JDescription mDescription;
    @SerializedName("project")
    private JProject mProject;
    @SerializedName("issuetype")
    private JIssueType mIssueType;
    @SerializedName("priority")
    private JPriority mPriority;

    public JFields() {

    }

    public JFields(JSummary summary, JAttachment attachment, JDescription description, JProject project, JIssueType issueType, JPriority priority) {
        this.mSummary = summary;
        this.mAttachment = attachment;
        this.mDescription = description;
        this.mProject = project;
        this.mIssueType = issueType;
        this.mPriority = priority;
    }

    public JSummary getSummary() {
        return mSummary;
    }

    public void setSummary(JSummary summary) {
        this.mSummary = summary;
    }

    public JAttachment getAttachment() {
        return mAttachment;
    }

    public void setAttachment(JAttachment attachment) {
        this.mAttachment = attachment;
    }

    public JDescription getDescription() {
        return mDescription;
    }

    public void setDescription(JDescription description) {
        this.mDescription = description;
    }

    public JProject getProject() {
        return mProject;
    }

    public void setProject(JProject project) {
        this.mProject = project;
    }

    public JIssueType getIssueType() {
        return mIssueType;
    }

    public void setIssueType(JIssueType issueType) {
        this.mIssueType = issueType;
    }

    public JPriority getPriority() {
        return mPriority;
    }

    public void setPriority(JPriority priority) {
        this.mPriority = priority;
    }
}
