package amtt.epam.com.amtt.bo;

import android.util.Log;

import amtt.epam.com.amtt.processing.IssueGsonProcessor;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class CreateIssue {

    private JiraProject mProject = new JiraProject();
    private JiraIssueType mIssuetype = new JiraIssueType();
    private JiraBaseFields mFields = new JiraBaseFields();
    private JiraBase mBData = new JiraBase();
    private IssueGsonProcessor<JiraBase> mIssueGsonProcessor = new IssueGsonProcessor<>();
    private String jsonString = null;

    public CreateIssue() {
    }

    public String createSimpleIssue(String keyProject, String issueTypeName, String summary, String description) {
        mProject.setKeyProject(keyProject);
        mIssuetype.setName(issueTypeName);
        mFields.setProject(mProject);
        mFields.setSummary(summary);
        mFields.setDescription(description);
        mFields.setIssuetype(mIssuetype);
        mBData.setFields(mFields);
        try {
            jsonString = mIssueGsonProcessor.process(mBData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("test", jsonString);
        return jsonString;
    }
}
