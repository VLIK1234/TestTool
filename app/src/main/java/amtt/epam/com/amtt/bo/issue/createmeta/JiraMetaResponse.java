package amtt.epam.com.amtt.bo.issue.createmeta;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JiraMetaResponse {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("projects")
    private ArrayList<JProjects> mProjects;

    public JiraMetaResponse() {

    }

    public JiraMetaResponse(String expand, ArrayList<JProjects> projects) {
        this.mExpand = expand;
        this.mProjects = projects;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }

    public ArrayList<JProjects> getProjects() {
        return mProjects;
    }

    public void setProjects(ArrayList<JProjects> projects) {
        this.mProjects = projects;
    }

    public ArrayList<String> getProjectsNames() {
        ArrayList<String> projectsNames = new ArrayList<>();
        int size = mProjects.size();
        for (int i = 0; i < size; i++) {
            projectsNames.add(mProjects.get(i).getName());
        }
        return projectsNames;
    }



    public ArrayList<String> getProjectsKeys() {
        ArrayList<String> projectsKeys = new ArrayList<>();
        int size = mProjects.size();
        for (int i = 0; i < size; i++) {
            projectsKeys.add(mProjects.get(i).getKey());
        }
        return projectsKeys;
    }
}
