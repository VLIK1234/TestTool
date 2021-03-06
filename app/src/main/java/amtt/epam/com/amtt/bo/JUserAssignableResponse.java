package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.user.JUser;

import java.util.ArrayList;
import java.util.List;

/**
 @author Iryna Monchanka
 @version on 04.05.2015
 */

public class JUserAssignableResponse {

    private List<JUser> mAssignableUsers;

    public JUserAssignableResponse(){}

    public void setAssignableUsers(List<JUser> assignableUsers) {
        this.mAssignableUsers = assignableUsers;
    }

    public List<String> getAssignableUsersNames() {
        if (mAssignableUsers != null) {
            List<String> mAssignableUsersNames = new ArrayList<>();
            for (int i = 0; i < mAssignableUsers.size(); i++) {
                mAssignableUsersNames.add(mAssignableUsers.get(i).getName());
            }
            return mAssignableUsersNames;
        } else {
            return null;
        }
    }
}
