package amtt.epam.com.amtt.callbacks;

import amtt.epam.com.amtt.bo.issue.createmeta.JiraMetaResponse;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public interface ShowUserDataCallback {
    void onShowUserDataResult(JiraMetaResponse result);

}
