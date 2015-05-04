package amtt.epam.com.amtt.bo;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JProjectExtVersionsResponse {

    private ArrayList<JPriorityResponse> mVersions;

    public JProjectExtVersionsResponse(){}

    public JProjectExtVersionsResponse(ArrayList<JPriorityResponse> versions) {
        this.mVersions = versions;
    }

    public ArrayList<JPriorityResponse> getVersions() {
        return mVersions;
    }

    public void setVersions(ArrayList<JPriorityResponse> versions) {
        this.mVersions = versions;
    }
}
