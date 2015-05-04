package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.user.JiraUser;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JUserAssignableResponse {

    private ArrayList<JiraUser> mAssignableUsers;

    public JUserAssignableResponse(ArrayList<JiraUser> assignableUsers) {
        this.mAssignableUsers = assignableUsers;
    }

    public ArrayList<JiraUser> getAssignableUsers() {
        return mAssignableUsers;
    }

    public void setAssignableUsers(ArrayList<JiraUser> assignableUsers) {
        this.mAssignableUsers = assignableUsers;
    }
}
