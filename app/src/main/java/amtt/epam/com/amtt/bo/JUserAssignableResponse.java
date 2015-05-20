package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.user.JUser;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JUserAssignableResponse {

    private ArrayList<JUser> mAssignableUsers;
    private ArrayList<String> mAssignableUsersNames;

    public JUserAssignableResponse(){}

    public JUserAssignableResponse(ArrayList<JUser> assignableUsers) {
        this.mAssignableUsers = assignableUsers;
    }

    public ArrayList<JUser> getAssignableUsers() {
        return mAssignableUsers;
    }

    public void setAssignableUsers(ArrayList<JUser> assignableUsers) {
        this.mAssignableUsers = assignableUsers;
    }

    public ArrayList<String> getAssignableUsersNames() {
        if (mAssignableUsers != null) {
            mAssignableUsersNames = new ArrayList<>();
            for (int i = 0; i < mAssignableUsers.size(); i++) {
                mAssignableUsersNames.add(mAssignableUsers.get(i).getName());
            }
            return  mAssignableUsersNames;
        } else {
            return null;
        }
    }
}
