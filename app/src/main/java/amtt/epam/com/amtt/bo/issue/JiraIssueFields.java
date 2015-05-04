package amtt.epam.com.amtt.bo.issue;

import amtt.epam.com.amtt.bo.project.JiraIssueVersion;
import amtt.epam.com.amtt.bo.user.JiraUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irina Monchenko on 26.03.2015.
 */
public class JiraIssueFields {

    @SerializedName("project")
    private JiraIssueProject mProject;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("issuetype")
    private JiraIssueTypesIssueType mIssueType;
    @SerializedName("assignee")
    private JiraUser mAssignee;
    @SerializedName("reporter")
    private JiraUser mReporter;
    @SerializedName("priority")
    private JiraIssuePriority mPriority;
    @SerializedName("labels")
    private ArrayList<String> mLabels = new ArrayList<>();
    @SerializedName("timetracking")
    private JiraIssueTimeTracking mJiraIssueTimeTracking;
    @SerializedName("security")
    private JiraIssueSecurity mJiraIssueSecurity;
    @SerializedName("versions")
    private ArrayList<JiraIssueVersion> mJiraIssueVersions;
    @SerializedName("environment")
    private String mEnvironment;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("duedate")
    private String mDueDate;
    @SerializedName("fixVersions")
    private ArrayList<JiraIssueVersion> mJiraIssueFixVersions;
    @SerializedName("components")
    private ArrayList<JiraIssueComponent> mJiraIssueComponents;

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


    public JiraIssueFields() {
    }

    public JiraIssueFields(JiraIssueProject project, String summary, String description, JiraIssueTypesIssueType issueType) {
        this.mProject = project;
        this.mSummary = summary;
        this.mDescription = description;
        this.mIssueType = issueType;
    }

    public JiraIssueFields(JiraIssueProject mProject, String mSummary, JiraIssueTypesIssueType mIssueType, JiraUser mAssignee,
                           JiraUser mReporter, JiraIssuePriority mPriority, ArrayList<String> labels, JiraIssueTimeTracking jiraIssueTimeTracking,
                           JiraIssueSecurity jiraIssueSecurity, ArrayList<JiraIssueVersion> jiraIssueVersions, String mEnvironment, String mDescription,
                           String mDueDate, ArrayList<JiraIssueVersion> jiraIssueFixVersions, ArrayList<JiraIssueComponent> jiraIssueComponents) {
        this.mProject = mProject;
        this.mSummary = mSummary;
        this.mIssueType = mIssueType;
        this.mAssignee = mAssignee;
        this.mReporter = mReporter;
        this.mPriority = mPriority;
        this.mLabels = labels;
        this.mJiraIssueTimeTracking = jiraIssueTimeTracking;
        this.mJiraIssueSecurity = jiraIssueSecurity;
        this.mJiraIssueVersions = jiraIssueVersions;
        this.mEnvironment = mEnvironment;
        this.mDescription = mDescription;
        this.mDueDate = mDueDate;
        this.mJiraIssueFixVersions = jiraIssueFixVersions;
        this.mJiraIssueComponents = jiraIssueComponents;
    }

    public JiraIssueProject getProject() {
        return mProject;
    }

    public void setProject(JiraIssueProject project) {
        this.mProject = project;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public JiraIssueTypesIssueType getIssueType() {
        return mIssueType;
    }

    public void setIssueType(JiraIssueTypesIssueType issueType) {
        this.mIssueType = issueType;
    }

    public JiraUser getAssignee() {
        return mAssignee;
    }

    public void setAssignee(JiraUser assignee) {
        this.mAssignee = assignee;
    }

    public JiraUser getReporter() {
        return mReporter;
    }

    public void setReporter(JiraUser reporter) {
        this.mReporter = reporter;
    }

    public JiraIssuePriority getPriority() {
        return mPriority;
    }

    public void setPriority(JiraIssuePriority priority) {
        this.mPriority = priority;
    }

    public ArrayList<String> getLabels() {
        return mLabels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.mLabels = labels;
    }

    public JiraIssueTimeTracking getJiraIssueTimeTracking() {
        return mJiraIssueTimeTracking;
    }

    public void setJiraIssueTimeTracking(JiraIssueTimeTracking jiraIssueTimeTracking) {
        this.mJiraIssueTimeTracking = jiraIssueTimeTracking;
    }

    public JiraIssueSecurity getJiraIssueSecurity() {
        return mJiraIssueSecurity;
    }

    public void setJiraIssueSecurity(JiraIssueSecurity jiraIssueSecurity) {
        this.mJiraIssueSecurity = jiraIssueSecurity;
    }

    public ArrayList<JiraIssueVersion> getJiraIssueVersions() {
        return mJiraIssueVersions;
    }

    public void setJiraIssueVersions(ArrayList<JiraIssueVersion> jiraIssueVersions) {
        this.mJiraIssueVersions = jiraIssueVersions;
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

    public ArrayList<JiraIssueVersion> getJiraIssueFixVersions() {
        return mJiraIssueFixVersions;
    }

    public void setJiraIssueFixVersions(ArrayList<JiraIssueVersion> jiraIssueFixVersions) {
        this.mJiraIssueFixVersions = jiraIssueFixVersions;
    }

    public ArrayList<JiraIssueComponent> getJiraIssueComponents() {
        return mJiraIssueComponents;
    }

    public void setJiraIssueComponents(ArrayList<JiraIssueComponent> jiraIssueComponents) {
        this.mJiraIssueComponents = jiraIssueComponents;
    }
}


