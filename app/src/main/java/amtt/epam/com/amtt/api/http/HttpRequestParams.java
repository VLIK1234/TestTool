package amtt.epam.com.amtt.api.http;

import org.apache.http.HttpEntity;

import java.util.Map;

import amtt.epam.com.amtt.api.http.postexecution.HttpPostExecutionHandler;
import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Present parameters which are passed to the HttpClient
 */
public class HttpRequestParams<ResultType> {

    public static class Builder<ResultType> {

        private Map<String, String> mHeaders;
        private String mUrl;
        private Processor<ResultType, HttpEntity> mProcessor;
        private HttpEntity mPostEntity;
        private HttpPostExecutionHandler mPostExecutionHandler;

        public Builder setHeaders(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setProcessor(Processor<ResultType, HttpEntity> processor) {
            mProcessor = processor;
            return this;
        }

        public Builder setPostEntity(HttpEntity postEntity) {
            mPostEntity = postEntity;
            return this;
        }

        public Builder setPostExecutionHandler(HttpPostExecutionHandler postExecutionHandler) {
            mPostExecutionHandler = postExecutionHandler;
            return this;
        }

        public HttpRequestParams create() {
            HttpRequestParams<ResultType> requestParams = new HttpRequestParams<>();
            requestParams.mHeaders = this.mHeaders;
            requestParams.mUrl = this.mUrl;
            requestParams.mProcessor = this.mProcessor;
            requestParams.mPostEntity = this.mPostEntity;
            requestParams.mPostExecutionHandler = this.mPostExecutionHandler;
            return requestParams;
        }

    }

    private Map<String, String> mHeaders;
    private String mUrl;
    private Processor<ResultType, HttpEntity> mProcessor;
    private HttpEntity mPostEntity;
    private HttpPostExecutionHandler mPostExecutionHandler;

    /*
    * Set headers for current request
    * @param headers Headers for method in form (headerName, headerValue)
    * */
    public HttpRequestParams setHeaders(Map<String, String> headers) {
        mHeaders = headers;
        return this;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public HttpRequestParams setUrl(String url) {
        mUrl = url;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public HttpRequestParams setProcessor(Processor<ResultType, HttpEntity> processor) {
        mProcessor = processor;
        return this;
    }

    public Processor<ResultType, HttpEntity> getProcessor() {
        return mProcessor;
    }

    public HttpRequestParams setPostEntity(HttpEntity postEntity) {
        mPostEntity = postEntity;
        return this;
    }

    public HttpEntity getPostEntity() {
        return mPostEntity;
    }

    public HttpPostExecutionHandler getPostExecutionHandler() {
        return mPostExecutionHandler;
    }

}
