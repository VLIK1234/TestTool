package amtt.epam.com.amtt.bo.issue.createmeta;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JMetaResponse {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("projects")
    private ArrayList<JProjects> mProjects;

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
}
