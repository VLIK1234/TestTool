package amtt.epam.com.amtt.api;

import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestResponse;

/**
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public interface JiraCallback<T> {

    void onRequestStarted();

    void onRequestPerformed(RestResponse<?> restResponse);

    void onRequestError(AmttException e);

}
