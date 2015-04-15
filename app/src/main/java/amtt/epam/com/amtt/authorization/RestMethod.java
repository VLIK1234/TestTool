package amtt.epam.com.amtt.authorization;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

import amtt.epam.com.amtt.processing.ResponseProcessor;

/**
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestMethod {

    public static class Builder {

        private RestMethodType mRestMethodType;
        private Map<String, String> mHeaders;
        private String mUrl;
        private ResponseProcessor mProcessor;
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
            mProcessor = processor;
            return this;
        }

        public Builder setJsonString(String jsonString) {
            mJsonString = jsonString;
            return this;
        }

        public RestMethod create() {
            RestMethod restMethod = new RestMethod();
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
    private ResponseProcessor mProcessor;
    private String mJsonString;


    static {
        mHttpClient = new DefaultHttpClient();
    }

    private RestMethod() {
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

    public RestResponse execute() throws IOException {
        HttpResponse httpResponse = null;

        switch (mRestMethodType) {
            case GET:
                httpResponse = get();
                break;
            case POST:
                httpResponse = post();
        }

        String responseMessage;
        try {
            responseMessage = mProcessor.process(httpResponse);
        } catch (Exception e) {
            responseMessage = "response is illegible=(";
        }

        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        restResponse.setMessage(responseMessage);
        restResponse.setEntity(httpResponse.getEntity());
        return restResponse;
    }

}
