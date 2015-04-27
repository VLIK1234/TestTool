package amtt.epam.com.amtt.api;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestMethod.RestMethodType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */

@SuppressWarnings("unchecked")
public class JiraApi {

    private static class JiraApiSingletonHolder {

        public static final JiraApi INSTANCE = new JiraApi();

    }

    public static JiraApi getInstance() {
        return JiraApiSingletonHolder.INSTANCE;
    }

    private final CredentialsManager mCredentialsManager;
    private RestMethod mMethod;

    private JiraApi() {
        mCredentialsManager = CredentialsManager.getInstance();
    }

    public RestMethod buildAuth(final String userName, final String password, final String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mCredentialsManager.getCredentials(userName, password));
        mMethod = new RestMethod.Builder<String>()
                .setType(RestMethodType.GET)
                .setUrl(url + JiraApiConst.LOGIN_PATH)
                .setHeadersMap(headers)
                .setProcessor(new AuthResponseProcessor())
                .create();
        return mMethod;
    }

    public RestMethod buildIssueCeating(final String postEntity) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mCredentialsManager.getCredentials());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);
        mMethod = new RestMethod.Builder<Void>()
                .setType(RestMethodType.POST)
                .setUrl(mCredentialsManager.getUrl() + JiraApiConst.ISSUE_PATH)
                .setHeadersMap(headers)
                .setPostEntity(postEntity)
                .create();
        return mMethod;
    }

    public <ResultType, InputType> RestMethod buildDataSearch(final String requestSuffix, final Processor<ResultType, InputType> processor) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mCredentialsManager.getCredentials());

        mMethod = new RestMethod.Builder()
                .setType(RestMethodType.GET)
                .setUrl(mCredentialsManager.getUrl() + requestSuffix)
                .setHeadersMap(headers)
                .setProcessor(processor)
                .create();
        return mMethod;
    }

    public RestResponse execute() {
        RestResponse<Void> restResponse;
        try {
            restResponse = mMethod.execute();
        } catch (Exception e) {
            restResponse = new RestResponse<>();
            restResponse.setOperationResult(JiraOperationResult.UNPERFORMED);
            restResponse.setMessage(e);
            return restResponse;
        }
        restResponse.setOperationResult(JiraOperationResult.PERFORMED);
        return restResponse;
    }

}


