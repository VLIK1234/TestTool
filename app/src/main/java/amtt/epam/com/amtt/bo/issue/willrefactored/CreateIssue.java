package amtt.epam.com.amtt.bo.issue.willrefactored;

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
    private JCreatingIssueRequest mBData = new JCreatingIssueRequest();
    private IssueGsonProcessor<JCreatingIssueRequest> mIssueGsonProcessor = new IssueGsonProcessor<>();
    private String jsonString = null;

    public CreateIssue() {
    }

    public String createSimpleIssue(String keyProject, String issueTypeName, String summary, String description) {
        mProject.setKey(keyProject);
        mIssueType.setName(issueTypeName);
        mFields.setProject(mProject);
        mFields.setSummary(summary);
        mFields.setDescription(description);
        mFields.setIssueType(mIssueType);
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
