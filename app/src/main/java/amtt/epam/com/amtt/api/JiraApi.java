package amtt.epam.com.amtt.api;

import org.apache.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestMethod.RestMethodType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.CreateIssueResult;
import amtt.epam.com.amtt.api.result.JiraOpearationResult;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */

@SuppressWarnings("unchecked")
public class JiraApi {

    private static class JiraApiSingletonHolder {

        public static final JiraApi INSTANCE = new JiraApi(CredentialsManager.getInstance());

    }

    public static JiraApi getInstance() {
        return JiraApiSingletonHolder.INSTANCE;
    }

    private final String mUrl;
    private final String mCredentialsString;
    private RestMethod mMethod;

    private JiraApi(CredentialsManager credentialsManager) {
        mUrl = credentialsManager.getUrl();
        mCredentialsString = credentialsManager.getCredentials();
    }

    public JiraApi authorize() {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mCredentialsString);
        mMethod = new RestMethod.Builder<JiraOpearationResult, Void>()
                .setType(RestMethodType.GET)
                .setUrl(mUrl + JiraApiConst.LOGIN_PATH)
                .setHeadersMap(headers)
                .setResponseProcessor(new AuthResponseProcessor())
                .create();
        return this;
    }

    public JiraApi createIssue(final String jsonString) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mCredentialsString);
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);
        mMethod = new RestMethod.Builder<CreateIssueResult, Void>()
                .setType(RestMethodType.POST)
                .setUrl(mUrl + JiraApiConst.ISSUE_PATH)
                .setHeadersMap(headers)
                .setPostJson(jsonString)
                .create();
        return this;
    }

    public <ResultObjectType> JiraApi searchData(final String requestSuffix, final Processor<ResultObjectType, HttpEntity> processor) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mCredentialsString);
        String url = mUrl + requestSuffix;

        mMethod = new RestMethod.Builder()
                .setType(RestMethodType.GET)
                .setUrl(url + requestSuffix)
                .setHeadersMap(headers)
                .setObjectProcessor(processor)
                .create();
        return this;
    }

    public RestResponse execute() {
        RestResponse<JiraOpearationResult, Void> restResponse;
        try {
            restResponse = mMethod.execute();
        } catch (Exception e) {
            restResponse = new RestResponse<>();
            restResponse.setResult(JiraOpearationResult.UNPERFORMED);
            restResponse.setMessage(e);
            return restResponse;
        }
        restResponse.setResult(JiraOpearationResult.PERFORMED);
        return restResponse;
    }

}


