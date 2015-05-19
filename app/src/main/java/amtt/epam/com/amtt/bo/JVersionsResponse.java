package amtt.epam.com.amtt.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.bo.project.JiraIssueVersion;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JVersionsResponse {

    private ArrayList<JiraIssueVersion> mVersions;

    public JVersionsResponse(){}

    public JVersionsResponse(ArrayList<JiraIssueVersion> versions) {
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

    public ArrayList<JiraIssueVersion> getVersions() {
        return mVersions;
    }

    public void setVersions(ArrayList<JiraIssueVersion> versions) {
        this.mVersions = versions;
    }

    public JiraIssueVersion getIssueVersionByName(String versionName) {
        JiraIssueVersion issueVersion = null;
        for (JiraIssueVersion version : mVersions) {
            if (version.getName().equals(versionName)) {
                issueVersion = version;
            }
        }
        return issueVersion;
    }
}
