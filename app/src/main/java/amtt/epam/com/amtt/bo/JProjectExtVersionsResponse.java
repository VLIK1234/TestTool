package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.project.JiraIssueVersion;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JProjectExtVersionsResponse {

    private ArrayList<JiraIssueVersion> mVersions;
    private ArrayList<String> mVersionsNames;


    public JProjectExtVersionsResponse(){}

    public JProjectExtVersionsResponse(ArrayList<JiraIssueVersion> versions) {
        this.mVersions = versions;
    }

    public ArrayList<String> getVersionsNames(){
        if (mVersions != null) {
           mVersionsNames = new ArrayList<>();
            for (int i = 0; i < mVersions.size(); i++) {
                mVersionsNames.add(mVersions.get(i).getName());
            }
            return  mVersionsNames;
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
}
