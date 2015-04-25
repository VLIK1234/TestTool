package amtt.epam.com.amtt.api.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.processing.Processor;

/**
 * Class for performing REST methods to Jira api
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestMethod<OutputType> {

    public enum RestMethodType {

        GET,
        POST

    }

    public static class Builder<OutputType> {

        private RestMethodType mRestMethodType;
        private Map<String, String> mHeaders;
        private String mUrl;
        private Processor<OutputType, HttpEntity> mProcessor; //processor for retrieving OBJECTS
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

        public Builder setProcessor(Processor<OutputType, HttpEntity> processor) {
            mProcessor = processor;
            return this;
        }

        public Builder setPostJson(String jsonString) {
            mJsonString = jsonString;
            return this;
        }

        public RestMethod create() {
            RestMethod<OutputType> restMethod = new RestMethod<>();
            restMethod.mRestMethodType = this.mRestMethodType;
            restMethod.mHeaders = this.mHeaders;
            restMethod.mUrl = this.mUrl;
            restMethod.mProcessor = this.mProcessor;
            restMethod.mJsonString = this.mJsonString;
            return restMethod;
        }

    }

    private static HttpClient mHttpClient;
    private RestMethodType mRestMethodType;
    private Map<String, String> mHeaders;
    private String mUrl;
    private Processor<OutputType, HttpEntity> mProcessor;
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

        HttpResponse httpResponse;
        try {
            httpResponse = mHttpClient.execute(httpGet);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("jira domain is wrong");
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Check jira domain or internet connection");
        }
        return httpResponse;
    }

    private HttpResponse post() throws IOException {
        HttpPost httpPost = new HttpPost(mUrl);
        httpPost.setEntity(new StringEntity(mJsonString));
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpPost.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }
        return mHttpClient.execute(httpPost);
    }

    public RestResponse<OutputType> execute() throws Exception {
        HttpResponse httpResponse = null;

        switch (mRestMethodType) {
            case GET:
                httpResponse = get();
                break;
            case POST:
                httpResponse = post();
                break;
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_NOT_FOUND || statusCode == HttpStatus.SC_BAD_GATEWAY) {
            throw new AuthenticationException("Check jira domain");
        } else if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) {
            throw new AuthenticationException("Check password or user name");
        } else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            throw new Exception("Internal server error occurred");
        }

        RestResponse<OutputType> restResponse = new RestResponse<>();
        try {
            if (mProcessor != null) {
                OutputType result = mProcessor.process(httpResponse.getEntity());
                restResponse.setResultObject(result);
            }
        } catch (Exception e) {
            throw new Exception("Received json is illegible");
        }

        if (mRestMethodType == RestMethodType.POST) {
            restResponse.setOperationResult(JiraOperationResult.CREATED);
        }
        return restResponse;
    }

}
