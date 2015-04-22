package amtt.epam.com.amtt.api;

import amtt.epam.com.amtt.api.rest.RestResponse;

/**
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public interface JiraCallback<ResultType, ResultObjectType> {

    void onJiraRequestPerformed(RestResponse<ResultType, ResultObjectType> restResponse);

}
