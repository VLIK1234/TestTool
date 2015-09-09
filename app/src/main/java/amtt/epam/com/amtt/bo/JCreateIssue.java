package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.issue.JIssueFields;
import amtt.epam.com.amtt.bo.issue.JIssuePriority;
import amtt.epam.com.amtt.bo.issue.JIssueProject;
import amtt.epam.com.amtt.bo.issue.JIFIssueType;
import amtt.epam.com.amtt.bo.project.JIssueVersion;
import amtt.epam.com.amtt.bo.user.JUser;
import amtt.epam.com.amtt.processing.GsonSerializeProcessor;
import amtt.epam.com.amtt.util.Logger;

/**
 @author Iryna Monchanka
 @version on 3/26/2015
 */

public class JCreateIssue {

    private final String TAG = this.getClass().getSimpleName();
    private final JCreatingIssueRequest mBData = new JCreatingIssueRequest();
    private final GsonSerializeProcessor<JCreatingIssueRequest> mGsonSerializeProcessor = new GsonSerializeProcessor<>();

    public JCreateIssue(String keyProject, String issueTypeId, String summary, String description,
                        String priorityId, String versionsId, String environment, String userAssigneName, String componentIds) {
        JIssueProject mProject = new JIssueProject();
        mProject.setKey(keyProject);
        JIFIssueType mIssueType = new JIFIssueType();
        mIssueType.setJiraId(issueTypeId);
        JIssuePriority mPriority = new JIssuePriority();
        mPriority.setJiraId(priorityId);
        JIssueVersion mVersion = new JIssueVersion();
        mVersion.setJiraId(versionsId);
        JIssueFields mFields = new JIssueFields();
        if (userAssigneName != null) {
            JUser mAssignee = new JUser();
            mAssignee.setName(userAssigneName);
            mFields.setAssignee(mAssignee);
        }
        mFields.setProject(mProject);
        mFields.setIssueType(mIssueType);
        mFields.setPriority(mPriority);
        mFields.setJiraIssueVersions(mVersion);
        mFields.setSummary(summary);
        mFields.setDescription(description);
        mFields.setEnvironment(environment);
        mFields.setJiraIssueComponentsId(componentIds);
        mBData.setFields(mFields);
        getResultJson();
    }

    public String getResultJson() {
        String jsonString = null;
        try {
            jsonString = mGsonSerializeProcessor.process(mBData);
            Logger.d(TAG, jsonString);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage(), e);
        }
        return jsonString;
    }

}
