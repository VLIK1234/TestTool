package amtt.epam.com.amtt.bo.issue;

import amtt.epam.com.amtt.bo.project.JIssueVersion;
import amtt.epam.com.amtt.bo.user.JUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irina Monchenko on 26.03.2015.
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
    private ArrayList<String> mLabels = new ArrayList<>();
    @SerializedName("timetracking")
    private JIssueTimeTracking mJIssueTimeTracking;
    @SerializedName("security")
    private JIssueSecurity mJIssueSecurity;
    @SerializedName("versions")
    private ArrayList<JIssueVersion> mJIssueVersions = new ArrayList<>();
    @SerializedName("environment")
    private String mEnvironment;
    @SerializedName("summary")
    private String mDescription;
    @SerializedName("duedate")
    private String mDueDate;
    @SerializedName("fixVersions")
    private ArrayList<JIssueVersion> mJiraIssueFixVersions = new ArrayList<>();
    @SerializedName("components")
    private ArrayList<JIssueComponent> mJIssueComponents;

  /*  private String timespent;
 private String[] fixVersions;
  private String aggregatetimespent;
  private String resolution;
   private String resolutiondate;
   private int workratio;
   private String lastViewed;
    private JiraIssueWatches watches;
    private String created;
   private String customfield_10000;
  private String customfield_10001;
   private String customfield_10002;
 private String customfield_10003;
    private String customfield_10004;
   private String customfield_10007;
    private String customfield_10008;

    private String customfield_10012;
    private String customfield_10013;
   private String customfield_10014;
   private String customfield_10015;
   private String customfield_10016;
    private String customfield_10017;
   private String customfield_10018;
   private String customfield_10019;
   private String customfield_10020;
    private String customfield_10021;
    private String customfield_10022;
    private String customfield_10023;
    private String customfield_10024;
    private String[] labels;
    private int timeestimate;
    private int aggregatetimeoriginalestimate;
    private String[] versions;
    private String[] issuelinks;
    private JiraIssueAssignee assignee;
    private JiraIssuePriority priority;
    private String updated;
    private JiraIssueStatusCategory statusCategory;
    private String[] components;
    private String timeoriginalestimate;
    private String aggregatetimeestimate;
    private JiraIssueCreator creator;
    private String environment;
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
                        JUser mReporter, JIssuePriority mPriority, ArrayList<String> labels, JIssueTimeTracking jIssueTimeTracking,
                        JIssueSecurity jIssueSecurity, ArrayList<JIssueVersion> jIssueVersions, String mEnvironment, String mDescription,
                        String mDueDate, ArrayList<JIssueVersion> jiraIssueFixVersions, ArrayList<JIssueComponent> jIssueComponents) {
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
        this.mJIssueComponents = jIssueComponents;
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

    public ArrayList<String> getLabels() {
        return mLabels;
    }

    public void setLabels(ArrayList<String> labels) {
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

    public ArrayList<JIssueVersion> getJiraIssueVersions() {
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

    public ArrayList<JIssueVersion> getJiraIssueFixVersions() {
        return mJiraIssueFixVersions;
    }

    public void setJiraIssueFixVersions(ArrayList<JIssueVersion> jiraIssueFixVersions) {
        this.mJiraIssueFixVersions = jiraIssueFixVersions;
    }

    public ArrayList<JIssueComponent> getJiraIssueComponents() {
        return mJIssueComponents;
    }

    public void setJiraIssueComponents(ArrayList<JIssueComponent> jIssueComponents) {
        this.mJIssueComponents = jIssueComponents;
    }
}


