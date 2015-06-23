package amtt.epam.com.amtt.bo;

import java.util.ArrayList;
import java.util.HashMap;

import amtt.epam.com.amtt.bo.project.JIssueVersion;

/**
 @author Iryna Monchanka
 @version on 04.05.2015
 */

public class JVersionsResponse {

    private ArrayList<JIssueVersion> mVersions;

    public JVersionsResponse(){}

    public JVersionsResponse(ArrayList<JIssueVersion> versions) {
        this.mVersions = versions;
    }

    public HashMap<String, String> getVersionsNames(){
        HashMap<String, String> mVersionsNames;
        if (mVersions != null) {
            mVersionsNames = new HashMap<>();
            for (int i = 0; i < mVersions.size(); i++) {
                mVersionsNames.put(mVersions.get(i).getId(), mVersions.get(i).getName());
            }
            return mVersionsNames;
        } else {
            return null;
        }
    }

    public ArrayList<JIssueVersion> getVersions() {
        return mVersions;
    }

    public void setVersions(ArrayList<JIssueVersion> versions) {
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
