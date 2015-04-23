package amtt.epam.com.amtt.api;

import android.util.Base64;

import org.apache.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestMethod.RestMethodType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.AuthorizationResult;
import amtt.epam.com.amtt.api.result.CreateIssueResult;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */

@SuppressWarnings("unchecked")
public class JiraApi {
    //TODO need update when class goes to singleton
    final private String mUrl = CredentialsManager.getInstance().getUrl();
    private RestMethod mMethod;

    public void authorize() {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());
        mMethod = new RestMethod.Builder<AuthorizationResult, Void>()
                .setType(RestMethodType.GET)
                .setUrl(mUrl + JiraApiConst.LOGIN_PATH)
                .setHeadersMap(headers)
                .setResponseProcessor(new AuthResponseProcessor())
                .create();
    }

    public void createIssue(final String jsonString) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);
        mMethod = new RestMethod.Builder<CreateIssueResult, Void>()
                .setType(RestMethodType.POST)
                .setUrl(mUrl + JiraApiConst.ISSUE_PATH)
                .setHeadersMap(headers)
                .setJsonString(jsonString)
                .create();
//        } catch (Exception e) {
//            //TODO what if you get any error? no connection or other?  show reason if possible
//            restResponse = new RestResponse<>();
//            restResponse.setMessage("Issue isn't created: " + e.getMessage());
//            restResponse.setResult(CreateIssueResult.FAILURE);
//            return restResponse;
//        }
//        restResponse.setMessage("Issue is created");
//        restResponse.setResult(CreateIssueResult.SUCCESS);
//        return restResponse;
    }

    public <ResultObjectType> void searchData(final String requestSuffix, final Processor<ResultObjectType, HttpEntity> processor) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, getCredential());
        String url = mUrl + requestSuffix;

        mMethod = new RestMethod.Builder()
                .setType(RestMethodType.GET)
                .setUrl(url + requestSuffix)
                .setHeadersMap(headers)
                .setObjectProcessor(processor)
                .create();
//
//            restResponse.setResult(UserDataResult.SUCCESS);
//        } catch (Exception e) {
//            //TODO what if you get any error? no connection or other?  show reason if possible
//            restResponse = new RestResponse();
//            restResponse.setMessage(e);
//            restResponse.setResult(UserDataResult.FAILURE);
//        }
//        return restResponse;
    }

    public RestResponse execute() {
        RestResponse<AuthorizationResult, Void> restResponse;
        try {
            restResponse = mMethod.execute();
        } catch (Exception e) {
            restResponse = new RestResponse<>();
            restResponse.setResult(AuthorizationResult.DENIED);
            restResponse.setException(e);
            return restResponse;
        }
        restResponse.setResult(AuthorizationResult.SUCCESS);
        return restResponse;
    }

    public String getCredential() {
        //TODO Base64.encodeToString for every call?
        //TODO CredentialsManager.getInstance().getPassword() - can we store password?
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((CredentialsManager.getInstance().getUserName() +
                Constants.SharedPreferenceKeys.COLON + CredentialsManager.getInstance().getPassword()).getBytes(), Base64.NO_WRAP);
    }

}


