package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.issue.JiraIssuePriority;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.project.JiraIssueVersion;
import android.util.Log;

import amtt.epam.com.amtt.bo.JCreatingIssueRequest;
import amtt.epam.com.amtt.bo.issue.JiraIssueFields;
import amtt.epam.com.amtt.bo.issue.JiraIssueProject;
import amtt.epam.com.amtt.bo.issue.JiraIssueTypesIssueType;
import amtt.epam.com.amtt.bo.user.JiraUser;
import amtt.epam.com.amtt.processing.IssueGsonProcessor;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */

public class CreateIssue {

    private final String TAG = this.getClass().getSimpleName();
    private JiraIssueProject mProject = new JiraIssueProject();
    private JiraIssueTypesIssueType mIssueType = new JiraIssueTypesIssueType();
    private JiraIssueFields mFields = new JiraIssueFields();
    private JiraIssuePriority mPriority = new JiraIssuePriority();
    private JiraIssueVersion mVersion = new JiraIssueVersion();
    private JiraUser mAssignee = new JiraUser();
    private JCreatingIssueRequest mBData = new JCreatingIssueRequest();
    private IssueGsonProcessor<JCreatingIssueRequest> mIssueGsonProcessor = new IssueGsonProcessor<>();

    public CreateIssue(String keyProject, String issueTypeId, String summary, String description,
                                    String priorityId, String versionsId, String environment, String userAssigneName) {
        mProject.setKey(keyProject);
        mIssueType.setId(issueTypeId);
        mPriority.setId(priorityId);
        mVersion.setId(versionsId);
        mAssignee.setName(userAssigneName);
        mFields.setAssignee(mAssignee);
        mFields.setProject(mProject);
        mFields.setIssueType(mIssueType);
        mFields.setPriority(mPriority);
        mFields.setJiraIssueVersions(mVersion);
        mFields.setSummary(summary);
        mFields.setDescription(description);
        mFields.setEnvironment(environment);
        mBData.setFields(mFields);
        getResultJson();
    }

    public String getResultJson() {
        String jsonString = null;
        try {
            jsonString = mIssueGsonProcessor.process(mBData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
