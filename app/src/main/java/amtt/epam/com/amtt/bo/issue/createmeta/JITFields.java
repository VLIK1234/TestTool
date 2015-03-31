package amtt.epam.com.amtt.bo.issue.createmeta;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */

/**
 * JiraIssueTypesFields = JITFields
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

public class JITFields {

    @SerializedName("summary")
    private JITSummary mSummary;
    @SerializedName("attachment")
    private JITAttachment mAttachment;
    @SerializedName("description")
    private JITDescription mDescription;
    @SerializedName("project")
    private JITProject mProject;
    @SerializedName("issuetype")
    private JITIssueType mIssuetype;
    @SerializedName("priority")
    private JITPriority mPriority;

    public JITFields() {

    }

    public JITFields(JITSummary summary, JITAttachment attachment, JITDescription description, JITProject project, JITIssueType issuetype, JITPriority priority) {
        this.mSummary = summary;
        this.mAttachment = attachment;
        this.mDescription = description;
        this.mProject = project;
        this.mIssuetype = issuetype;
        this.mPriority = priority;
    }

    public JITSummary getSummary() {
        return mSummary;
    }

    public void setSummary(JITSummary summary) {
        this.mSummary = summary;
    }

    public JITAttachment getAttachment() {
        return mAttachment;
    }

    public void setAttachment(JITAttachment attachment) {
        this.mAttachment = attachment;
    }

    public JITDescription getDescription() {
        return mDescription;
    }

    public void setDescription(JITDescription description) {
        this.mDescription = description;
    }

    public JITProject getProject() {
        return mProject;
    }

    public void setProject(JITProject project) {
        this.mProject = project;
    }

    public JITIssueType getIssuetype() {
        return mIssuetype;
    }

    public void setIssuetype(JITIssueType issuetype) {
        this.mIssuetype = issuetype;
    }

    public JITPriority getPriority() {
        return mPriority;
    }

    public void setPriority(JITPriority priority) {
        this.mPriority = priority;
    }
}
