package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.issue.JIssuePriority;
import amtt.epam.com.amtt.bo.project.JIssueVersion;

import amtt.epam.com.amtt.bo.issue.JIssueFields;
import amtt.epam.com.amtt.bo.issue.JIssueProject;
import amtt.epam.com.amtt.bo.issue.JIssueTypesIssueType;
import amtt.epam.com.amtt.bo.user.JUser;
import amtt.epam.com.amtt.processing.GsonSerializeProcessor;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */

public class JCreateIssue {

    private final String TAG = this.getClass().getSimpleName();
    private JIssueProject mProject = new JIssueProject();
    private JIssueTypesIssueType mIssueType = new JIssueTypesIssueType();
    private JIssueFields mFields = new JIssueFields();
    private JIssuePriority mPriority = new JIssuePriority();
    private JIssueVersion mVersion = new JIssueVersion();
    private JUser mAssignee = new JUser();
    private JCreatingIssueRequest mBData = new JCreatingIssueRequest();
    private GsonSerializeProcessor<JCreatingIssueRequest> mGsonSerializeProcessor = new GsonSerializeProcessor<>();

    public JCreateIssue(String keyProject, String issueTypeId, String summary, String description,
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
            jsonString = mGsonSerializeProcessor.process(mBData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
