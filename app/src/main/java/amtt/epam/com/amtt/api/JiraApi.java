package amtt.epam.com.amtt.api;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.AuthorizationResult;
import amtt.epam.com.amtt.api.JiraTask.JiraSearchType;
import amtt.epam.com.amtt.api.result.UserDataResult;
import amtt.epam.com.amtt.api.result.CreateIssueResult;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;
import amtt.epam.com.amtt.processing.ProjectsFromJsonProcessor;
import amtt.epam.com.amtt.processing.UserInfoFromJsonProcessor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.api.rest.RestMethod.RestMethodType;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */

@SuppressWarnings("unchecked")
public class JiraApi {
    //TODO need update when class goes to singleton
    final private String mUrl = CredentialsManager.getInstance().getUrl();

    //TODO divide request creation and execution. Pass created request to JiraTask to execute in background
    public RestResponse authorize() {
        //TODO we have base headers for every call?
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());

        RestResponse<AuthorizationResult, Void> restResponse;
        try {
            restResponse = new RestMethod.Builder<AuthorizationResult, Void>()
                    .setType(RestMethodType.GET)
                    .setUrl(mUrl + JiraApiConst.LOGIN_PATH)
                    .setHeadersMap(headers)
                    .setResponseProcessor(new AuthResponseProcessor())
                    .create()
                    .execute();
        } catch (Exception e) {
            //TODO what if you get any error? no connection or other?
            restResponse = new RestResponse<>();
            restResponse.setResult(AuthorizationResult.DENIED);
            restResponse.setMessage("Authorization isn't passed: " + e.getMessage());
            return restResponse;
        }

        //TODO what is bad gateway for user? what shall I do?
        //Bad gate way is considered as a httpResponse, not an exception
        if (restResponse.getStatusCode() == JiraApiConst.BAD_GATE_WAY) {
            restResponse.setMessage("Authorization isn't passed: bad gateway");
            return restResponse;
        }

        restResponse.setResult(AuthorizationResult.SUCCESS);
        return restResponse;
    }

    //TODO divide request creation and execution. Pass created request to JiraTask to execute in background
    public RestResponse createIssue(final String jsonString) {
        //TODO we have base headers for every call?
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);

        RestResponse<CreateIssueResult,Void> restResponse;
        try {
            restResponse = new RestMethod.Builder<CreateIssueResult,Void>()
                    .setType(RestMethodType.POST)
                    .setUrl(mUrl + JiraApiConst.ISSUE_PATH)
                    .setHeadersMap(headers)
                    .setJsonString(jsonString)
                    .create()
                    .execute();
        } catch (Exception e) {
            //TODO what if you get any error? no connection or other?  show reason if possible
            restResponse = new RestResponse<>();
            restResponse.setMessage("Issue isn't created: " + e.getMessage());
            restResponse.setResult(CreateIssueResult.FAILURE);
            return restResponse;
        }
        restResponse.setMessage("Issue is created");
        restResponse.setResult(CreateIssueResult.SUCCESS);
        return restResponse;
    }

    //TODO divide request creation and execution. Pass created request to JiraTask to execute in background
    //TODO we don't need username here. If we get one more type, we need to update this class?
    //pass url (or suffix) and processor to method will help you
    public RestResponse searchData(final String userName, final JiraSearchType typeData) {
        String url = null;
        RestMethod.Builder builder = new RestMethod.Builder();

        if (typeData != null) {
            switch (typeData) {
                case ISSUE: {
                    //TODO we don't need username here, why shall we pass it to method?
                    builder.setObjectProcessor(new ProjectsFromJsonProcessor());
                    url = mUrl + JiraApiConst.USER_PROJECTS_PATH;
                    break;
                }
                case USER_INFO:
                    builder.setObjectProcessor(new UserInfoFromJsonProcessor());
                    url = mUrl + JiraApiConst.USER_INFO_PATH + userName + JiraApiConst.EXPAND_GROUPS;
                    break;
            }
        }

        //TODO we have base headers for every call?
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());

        RestResponse restResponse;
        try {
            restResponse = builder
                    .setType(RestMethodType.GET)
                    .setUrl(url)
                    .setHeadersMap(headers)
                    .create()
                    .execute();

            restResponse.setResult(UserDataResult.SUCCESS);
        } catch (Exception e) {
            //TODO what if you get any error? no connection or other?  show reason if possible
            restResponse = new RestResponse();
            restResponse.setMessage(e);
            restResponse.setResult(UserDataResult.FAILURE);
        }
        return restResponse;
    }

    public String getCredential() {
        //TODO Base64.encodeToString for every call?
        //TODO CredentialsManager.getInstance().getPassword() - can we store password?
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((CredentialsManager.getInstance().getUserName() +
                Constants.SharedPreferenceKeys.COLON + CredentialsManager.getInstance().getPassword()).getBytes(), Base64.NO_WRAP);
    }

}


