package amtt.epam.com.amtt.callbacks;

import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public interface ShowUserInfoCallback {
    void onShowUserInfoResult(JiraUserInfo result);

}
