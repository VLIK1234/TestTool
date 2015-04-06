package amtt.epam.com.amtt.bo.issue.willrefactored;

import android.util.Log;

import amtt.epam.com.amtt.bo.issue.willrefactored.issuekey.JiraBaseFields;
import amtt.epam.com.amtt.bo.issue.willrefactored.issuekey.JiraIssueProject;
import amtt.epam.com.amtt.processing.IssueGsonProcessor;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class CreateIssue {

    private final String TAG = this.getClass().getSimpleName();
    private JiraIssueProject mProject = new JiraIssueProject();
    private JiraIssueTypesIssueType mIssueType = new JiraIssueTypesIssueType();
    private JiraBaseFields mFields = new JiraBaseFields();
    private JiraBase mBData = new JiraBase();
    private IssueGsonProcessor<JiraBase> mIssueGsonProcessor = new IssueGsonProcessor<>();
    private String jsonString = null;

    public CreateIssue() {
    }

    public String createSimpleIssue(String keyProject, String issueTypeName, String summary, String description) {
        mProject.setKeyProject(keyProject);
        mIssueType.setName(issueTypeName);
        mFields.setProject(mProject);
        mFields.setSummary(summary);
        mFields.setDescription(description);
        mFields.setIssuetype(mIssueType);
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
