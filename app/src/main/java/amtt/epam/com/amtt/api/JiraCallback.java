package amtt.epam.com.amtt.api;

import amtt.epam.com.amtt.api.rest.RestResponse;

/**
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public interface JiraCallback<ResultType> {

    void onRequestStarted();
    
    void onRequestPerformed(RestResponse<ResultType> restResponse);

    void onRequestError(Exception e);

}
