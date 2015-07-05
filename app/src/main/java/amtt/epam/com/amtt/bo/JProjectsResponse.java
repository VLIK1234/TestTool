package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public class JProjectsResponse {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("projects")
    private List<JProjects> mProjects;
    private HashMap<JProjects, String> mProjectsNames;
    private List<String> mProjectsKeys;

    public JProjectsResponse() {

    }

    public JProjectsResponse(String expand, List<JProjects> projects) {
        this.mExpand = expand;
        this.mProjects = projects;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }

    public List<JProjects> getProjects() {
        return mProjects;
    }

    public void setProjects(List<JProjects> projects) {
        this.mProjects = projects;
    }

    public HashMap<JProjects, String> getProjectsNames() {
        if (mProjects != null) {
            mProjectsNames = new HashMap<>();
            int size = mProjects.size();
            for (int i = 0; i < size; i++) {
                mProjectsNames.put(mProjects.get(i), mProjects.get(i).getName());
            }
        }
        return mProjectsNames;
    }

    public List<String> getProjectsKeys() {
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

    private void setProjectsKeys(List<String> projectsKeys) {
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
