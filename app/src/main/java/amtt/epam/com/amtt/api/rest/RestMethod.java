package amtt.epam.com.amtt.api.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.processing.ResponseProcessor;

/**
 * Class for performing REST methods to Jira api
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestMethod<ResultType, ResultObjectType> {

    public enum RestMethodType {

        GET,
        POST

    }

    public static class Builder<ResultType, ResultObjectType> {

        private RestMethodType mRestMethodType;
        private Map<String, String> mHeaders;
        private String mUrl;
        //TODO Why do we need two processors?
        private ResponseProcessor mResponseProcessor; //processor for retrieving String messages from Json responses
        private Processor<ResultObjectType, HttpEntity> mObjectProcessor; //processor for retrieving Objects from Json responses
        private String mJsonString;

        public Builder setType(RestMethodType methodType) {
            mRestMethodType = methodType;
            return this;
        }

        /*
        * Set headers for current method
        * @param headers Headers for method in form (headerName, headerValue)
        * */
        public Builder setHeadersMap(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setResponseProcessor(ResponseProcessor processor) {
            mResponseProcessor = processor;
            return this;
        }
        //TODO Why do we need two processors?
        public Builder setObjectProcessor(Processor<ResultObjectType, HttpEntity> processor) {
            mObjectProcessor = processor;
            return this;
        }

        //TODO is it post request body? why is it called so?
        public Builder setJsonString(String jsonString) {
            mJsonString = jsonString;
            return this;
        }

        public RestMethod create() {
            RestMethod<ResultType, ResultObjectType> restMethod = new RestMethod<>();
            restMethod.mRestMethodType = this.mRestMethodType;
            restMethod.mHeaders = this.mHeaders;
            restMethod.mUrl = this.mUrl;
            restMethod.mResponseProcessor = this.mResponseProcessor;
            restMethod.mObjectProcessor = this.mObjectProcessor;
            restMethod.mJsonString = this.mJsonString;
            return restMethod;
        }

    }

    private static HttpClient mHttpClient;
    private RestMethodType mRestMethodType;
    private Map<String, String> mHeaders;
    private String mUrl;
    private ResponseProcessor mResponseProcessor;
    private Processor<ResultObjectType, HttpEntity> mObjectProcessor;
    private String mJsonString;


    static {
        mHttpClient = new DefaultHttpClient();
    }

    public RestMethod() {
    }

    private HttpResponse get() throws IOException {
        HttpGet httpGet = new HttpGet(mUrl);
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpGet.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }
        return mHttpClient.execute(httpGet);
    }

    private HttpResponse post() throws IOException {
        HttpPost httpPost = new HttpPost(mUrl);
        httpPost.setEntity(new StringEntity(mJsonString));
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpPost.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }
        return mHttpClient.execute(httpPost);
    }

    public RestResponse<ResultType, ResultObjectType> execute() throws Exception {
        HttpResponse httpResponse = null;

        switch (mRestMethodType) {
            case GET:
                httpResponse = get();
                break;
            case POST:
                httpResponse = post();
                break;
        }

        String responseMessage = null;
        if (mResponseProcessor != null) {
            responseMessage = mResponseProcessor.process(httpResponse);
        }

        ResultObjectType resultObject = null;
        if (mObjectProcessor != null) {
            try {
                resultObject = mObjectProcessor.process(httpResponse.getEntity());
            } catch (Exception e) {
                responseMessage = "response object is illegible=(";
            }
        }

        //TODO I think it would be a good idea to have status code, headers, error and result.
        //TODO don't set entity. process it and close as soon it is not used later
        RestResponse<ResultType, ResultObjectType> restResponse = new RestResponse<>();
        restResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        restResponse.setMessage(responseMessage);
        restResponse.setEntity(httpResponse.getEntity());
        restResponse.setResultObject(resultObject);
        return restResponse;
    }

}
