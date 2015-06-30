package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import amtt.epam.com.amtt.bo.project.JComponent;
import amtt.epam.com.amtt.bo.project.JIssueVersion;
import amtt.epam.com.amtt.bo.user.JUser;

/**
 @author Iryna Monchanka
 @version on 26.03.2015
 */

public class JIssueFields {

    @SerializedName("project")
    private JIssueProject mProject;
    @SerializedName("description")
    private String mSummary;
    @SerializedName("issuetype")
    private JIssueTypesIssueType mIssueType;
    @SerializedName("assignee")
    private JUser mAssignee;
    @SerializedName("reporter")
    private JUser mReporter;
    @SerializedName("priority")
    private JIssuePriority mPriority;
    @SerializedName("labels")
    private List<String> mLabels;
    @SerializedName("timetracking")
    private JIssueTimeTracking mJIssueTimeTracking;
    @SerializedName("security")
    private JIssueSecurity mJIssueSecurity;
    @SerializedName("versions")
    private List<JIssueVersion> mJIssueVersions;
    @SerializedName("environment")
    private String mEnvironment;
    @SerializedName("summary")
    private String mDescription;
    @SerializedName("duedate")
    private String mDueDate;
    @SerializedName("fixVersions")
    private List<JIssueVersion> mJiraIssueFixVersions;
    @SerializedName("components")
    private List<JComponent> mJComponents;

  /*  private String timespent;
 private String[] fixVersions;
  private String aggregatetimespent;
  private String resolution;
   private String resolutiondate;
   private int workratio;
   private String lastViewed;
    private JiraIssueWatches watches;
    private String created;
    private String[] labels;
    private int timeestimate;
    private int aggregatetimeoriginalestimate;
    private String[] issuelinks;
    private String updated;
    private String timeoriginalestimate;
    private String aggregatetimeestimate;
    private String duedate;
    private JiraIssueProgress aggregateprogress;
    private JiraIssueProgress progress;
    private JiraIssueVotes votes;*/


    public JIssueFields() {
    }

    public JIssueFields(JIssueProject project, String summary, String description, JIssueTypesIssueType issueType) {
        this.mProject = project;
        this.mSummary = summary;
        this.mDescription = description;
        this.mIssueType = issueType;
    }

    public JIssueFields(JIssueProject mProject, String mSummary, JIssueTypesIssueType mIssueType, JUser mAssignee,
                        JUser mReporter, JIssuePriority mPriority, List<String> labels, JIssueTimeTracking jIssueTimeTracking,
                        JIssueSecurity jIssueSecurity, List<JIssueVersion> jIssueVersions, String mEnvironment, String mDescription,
                        String mDueDate, List<JIssueVersion> jiraIssueFixVersions, List<JComponent> jComponents) {
        this.mProject = mProject;
        this.mSummary = mSummary;
        this.mIssueType = mIssueType;
        this.mAssignee = mAssignee;
        this.mReporter = mReporter;
        this.mPriority = mPriority;
        this.mLabels = labels;
        this.mJIssueTimeTracking = jIssueTimeTracking;
        this.mJIssueSecurity = jIssueSecurity;
        this.mJIssueVersions = jIssueVersions;
        this.mEnvironment = mEnvironment;
        this.mDescription = mDescription;
        this.mDueDate = mDueDate;
        this.mJiraIssueFixVersions = jiraIssueFixVersions;
        this.mJComponents = jComponents;
    }

    public JIssueProject getProject() {
        return mProject;
    }

    public void setProject(JIssueProject project) {
        this.mProject = project;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public JIssueTypesIssueType getIssueType() {
        return mIssueType;
    }

    public void setIssueType(JIssueTypesIssueType issueType) {
        this.mIssueType = issueType;
    }

    public JUser getAssignee() {
        return mAssignee;
    }

    public void setAssignee(JUser assignee) {
        this.mAssignee = assignee;
    }

    public JUser getReporter() {
        return mReporter;
    }

    public void setReporter(JUser reporter) {
        this.mReporter = reporter;
    }

    public JIssuePriority getPriority() {
        return mPriority;
    }

    public void setPriority(JIssuePriority priority) {
        this.mPriority = priority;
    }

    public List<String> getLabels() {
        return mLabels;
    }

    public void setLabels(List<String> labels) {
        this.mLabels = labels;
    }

    public JIssueTimeTracking getJiraIssueTimeTracking() {
        return mJIssueTimeTracking;
    }

    public void setJiraIssueTimeTracking(JIssueTimeTracking jIssueTimeTracking) {
        this.mJIssueTimeTracking = jIssueTimeTracking;
    }

    public JIssueSecurity getJiraIssueSecurity() {
        return mJIssueSecurity;
    }

    public void setJiraIssueSecurity(JIssueSecurity jIssueSecurity) {
        this.mJIssueSecurity = jIssueSecurity;
    }

    public List<JIssueVersion> getJiraIssueVersions() {
        return mJIssueVersions;
    }

    public void setJiraIssueVersions(JIssueVersion jIssueVersions) {
        if (jIssueVersions != null) {
            mJIssueVersions.add(jIssueVersions);
        }
    }

    public String getEnvironment() {
        return mEnvironment;
    }

    public void setEnvironment(String environment) {
        this.mEnvironment = environment;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public void setDueDate(String dueDate) {
        this.mDueDate = dueDate;
    }

    public List<JIssueVersion> getJiraIssueFixVersions() {
        return mJiraIssueFixVersions;
    }

    public void setJiraIssueFixVersions(List<JIssueVersion> jiraIssueFixVersions) {
        this.mJiraIssueFixVersions = jiraIssueFixVersions;
    }

    public List<JComponent> getJiraIssueComponents() {
        return mJComponents;
    }

    public void setJiraIssueComponents(List<JComponent> jComponents) {
        this.mJComponents = jComponents;
    }

    public void setJiraIssueComponentsItem(JComponent component) {
        this.mJComponents.add(component);
    }

    public void setJiraIssueComponentsIds(List<String> componentsIds) {
        if (componentsIds != null) {
            if (componentsIds.size() != 0) {
                for (int i = 0; i < componentsIds.size(); i++) {
                    setJiraIssueComponentsItem(new JComponent(componentsIds.get(i)));
                }
            }
        }
    }

}


