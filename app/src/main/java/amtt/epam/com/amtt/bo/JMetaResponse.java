package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JMetaResponse {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("projects")
    private ArrayList<JProjects> mProjects;
    private ArrayList<String> mProjectsNames;
    private ArrayList<String> mProjectsKeys;

    public JMetaResponse() {

    }

    public JMetaResponse(String expand, ArrayList<JProjects> projects) {
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
        if (mProjectsNames == null) {
            mProjectsNames = new ArrayList<>();
            int size = mProjects.size();
            for (int i = 0; i < size; i++) {
                mProjectsNames.add(mProjects.get(i).getName());
            }
            setProjectsNames(mProjectsNames);
        }
        return mProjectsNames;
    }

    private void setProjectsNames(ArrayList<String> projectsNames) {
        this.mProjectsNames = projectsNames;
    }

    public ArrayList<String> getProjectsKeys() {
        if (mProjectsKeys == null) {
            mProjectsKeys = new ArrayList<>();
            int size = mProjects.size();
            for (int i = 0; i < size; i++) {
                mProjectsKeys.add(mProjects.get(i).getKey());
            }
            setProjectsKeys(mProjectsKeys);
        }
        return mProjectsKeys;
    }

    private void setProjectsKeys(ArrayList<String> projectsKeys) {
        this.mProjectsKeys = projectsKeys;
    }

    public JProjects getProjectByName(String projectName) {
        JProjects jProjects = null;
        for (JProjects project : mProjects) {
            if (project.getName().equals(projectName)) {
                jProjects = project;
            }
        }
        return jProjects;
    }

    public JProjects getProjectByKey(String projectKey) {
        JProjects jProjects = null;
        for (JProjects project : mProjects) {
            if (project.getKey().equals(projectKey)) {
                jProjects = project;
            }
        }
        return jProjects;
    }

}
