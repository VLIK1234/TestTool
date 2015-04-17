package amtt.epam.com.amtt.api;

import amtt.epam.com.amtt.authorization.RestResponse;

/**
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public interface JiraCallback<ResultType>{

    void onJiraRequestPerformed(RestResponse<ResultType> restResponse);

}
