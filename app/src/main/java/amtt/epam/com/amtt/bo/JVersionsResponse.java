package amtt.epam.com.amtt.bo;

import java.util.HashMap;
import java.util.List;

import amtt.epam.com.amtt.bo.project.JIssueVersion;

/**
 @author Iryna Monchanka
 @version on 04.05.2015
 */

public class JVersionsResponse {

    private List<JIssueVersion> mVersions;

    public JVersionsResponse(){}

    public HashMap<String, String> getVersionsNames(){
        HashMap<String, String> mVersionsNames;
        if (mVersions != null) {
            mVersionsNames = new HashMap<>();
            for (int i = 0; i < mVersions.size(); i++) {
                mVersionsNames.put(mVersions.get(i).getJiraId(), mVersions.get(i).getName());
            }
            return mVersionsNames;
        } else {
            return null;
        }
    }

    public List<JIssueVersion> getVersions() {
        return mVersions;
    }

    public void setVersions(List<JIssueVersion> versions) {
        this.mVersions = versions;
    }

    public JIssueVersion getIssueVersionByName(String versionName) {
        JIssueVersion issueVersion = null;
        for (JIssueVersion version : mVersions) {
            if (version.getName().equals(versionName)) {
                issueVersion = version;
            }
        }
        return issueVersion;
    }
}
