package amtt.epam.com.amtt.authorization;

import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
public class JiraApi {

    final private String mUrl = CredentialsManager.getInstance().getUrl();

    @SuppressWarnings("unchecked")
    public RestResponse authorize(final String userName, final String password, final String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());

        RestResponse<AuthorizationResult> restResponse;
        try {
            restResponse = new RestMethod.Builder<AuthorizationResult>()
                    .setType(RestMethodType.GET)
                    .setUrl(url + JiraApiConst.LOGIN_PATH)
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

    @SuppressWarnings("unchecked")
    public RestResponse createIssue(final String userName, final String password, final String url, final String jsonString) {
        String credentials = JiraApiConst.BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);

        RestResponse<CreationIssueResult> restResponse;
        try {
            restResponse = new RestMethod.Builder<CreationIssueResult>()
                    .setType(RestMethodType.POST)
                    .setUrl(mUrl + JiraApiConst.ISSUE_PATH)
                    .setHeadersMap(headers)
                    .setJsonString(jsonString)
                    .create()
                    .execute();
        } catch (Exception e) {
            restResponse = new RestResponse<>();
            restResponse.setMessage("Issue isn't created: " + e.getMessage());
            restResponse.setResult(CreationIssueResult.CREATION_UNSUCCESS);
            return restResponse;
        }
        restResponse.setMessage("Issue is created");
        restResponse.setResult(CreationIssueResult.CREATION_SUCCESS);
        return restResponse;
    }

//    public HttpEntity searchIssue(final String userName, final TypeSearchedData typeSearchData) throws Exception {
//        Map<String, String> headers = new HashMap<>();
//        headers.put(JiraApiConst.AUTH, getCredential());
//
//        RestResponse restResponse;
//        try {
//            restResponse = new RestMethod.Builder()
//                    .setType(RestMethodType.GET)
//                    .setUrl(url + JiraApiConst.USER_PROJECTS_PATH)
//                    .setHeadersMap(headers)
//                    .create()
//                    .execute();
//        } catch (Exception e) {
//            restResponse = new RestResponse();
//            restResponse.setMessage(e);
//        }
//        return restResponse.getEntity();
//    }

    public HttpEntity searchData(final String userName, final TypeSearchedData typeData) throws Exception {
        String url = null;
        if (typeData != null) {
            switch (typeData) {
                case SEARCH_ISSUE: {
                    url = mUrl + JiraApiConst.USER_PROJECTS_PATH;
                    break;
                }
                case SEARCH_USER_INFO:
                    url = mUrl + JiraApiConst.USER_INFO_PATH + userName + JiraApiConst.EXPAND_GROUPS;
                    break;
            }
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());

        RestResponse restResponse;
        try {
            restResponse = new RestMethod.Builder()
                    .setType(RestMethodType.GET)
                    .setUrl(url + JiraApiConst.USER_PROJECTS_PATH)
                    .setHeadersMap(headers)
                    .create()
                    .execute();
        } catch (Exception e) {
            restResponse = new RestResponse();
            restResponse.setMessage(e);
        }
        return restResponse.getEntity();
    }

    public String getCredential(){
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((CredentialsManager.getInstance().getUserName() +
                Constants.SharedPreferenceKeys.COLON + CredentialsManager.getInstance().getPassword()).getBytes(), Base64.NO_WRAP);
    }

}


