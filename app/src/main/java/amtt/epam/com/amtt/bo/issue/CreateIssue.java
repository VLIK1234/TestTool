package amtt.epam.com.amtt.bo.issue;

import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.project.JiraIssueVersion;
import android.util.Log;

import amtt.epam.com.amtt.bo.JCreatingIssueRequest;
import amtt.epam.com.amtt.bo.issue.JiraIssueFields;
import amtt.epam.com.amtt.bo.issue.JiraIssueProject;
import amtt.epam.com.amtt.bo.issue.JiraIssueTypesIssueType;
import amtt.epam.com.amtt.processing.IssueGsonProcessor;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
//todo Irina refactor after discuss with Alex D
public class CreateIssue {

    private final String TAG = this.getClass().getSimpleName();
    private JiraIssueProject mProject = new JiraIssueProject();
    private JiraIssueTypesIssueType mIssueType = new JiraIssueTypesIssueType();
    private JiraIssueFields mFields = new JiraIssueFields();
    private JiraIssuePriority mPriority = new JiraIssuePriority();
    private JiraIssueVersion mVersion = new JiraIssueVersion();

    private JCreatingIssueRequest mBData = new JCreatingIssueRequest();
    private IssueGsonProcessor<JCreatingIssueRequest> mIssueGsonProcessor = new IssueGsonProcessor<>();
    private String jsonString = null;

    public CreateIssue() {
    }

    public String createSimpleIssue(String keyProject, String issueTypeId, String summary, String description,
                                    String priorityId, String versionsId, String environment) {
        mProject.setKey(keyProject);
        mIssueType.setId(issueTypeId);
        mPriority.setId(priorityId);
        mVersion.setId(versionsId);

        mFields.setProject(mProject);
        mFields.setSummary(summary);
        mFields.setDescription(description);
        mFields.setIssueType(mIssueType);
        mFields.setPriority(mPriority);
        mFields.setJiraIssueVersions(mVersion);
        mFields.setEnvironment(environment);

        mBData.setFields(mFields);
        try {
            jsonString = mIssueGsonProcessor.process(mBData);
            Log.d(TAG, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
