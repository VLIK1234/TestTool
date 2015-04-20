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

    final private String mUrl = CredentialsManager.getInstance().getUrl();

    public RestResponse authorize() {
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
            restResponse = new RestResponse<>();
            restResponse.setResult(AuthorizationResult.DENIED);
            restResponse.setMessage("Authorization isn't passed: " + e.getMessage());
            return restResponse;
        }

        //Bad gate way is considered as a httpResponse, not an exception
        if (restResponse.getStatusCode() == JiraApiConst.BAD_GATE_WAY) {
            restResponse.setMessage("Authorization isn't passed: bad gateway");
            return restResponse;
        }

        restResponse.setResult(AuthorizationResult.SUCCESS);
        return restResponse;
    }

    public RestResponse createIssue(final String jsonString) {
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
            restResponse = new RestResponse<>();
            restResponse.setMessage("Issue isn't created: " + e.getMessage());
            restResponse.setResult(CreateIssueResult.FAILURE);
            return restResponse;
        }
        restResponse.setMessage("Issue is created");
        restResponse.setResult(CreateIssueResult.SUCCESS);
        return restResponse;
    }

    public RestResponse searchData(final String userName, final JiraSearchType typeData) {
        String url = null;
        RestMethod.Builder builder = new RestMethod.Builder();

        if (typeData != null) {
            switch (typeData) {
                case ISSUE: {
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
            restResponse = new RestResponse();
            restResponse.setMessage(e);
            restResponse.setResult(UserDataResult.FAILURE);
        }
        return restResponse;
    }

    public String getCredential() {
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((CredentialsManager.getInstance().getUserName() +
                Constants.SharedPreferenceKeys.COLON + CredentialsManager.getInstance().getPassword()).getBytes(), Base64.NO_WRAP);
    }

}


