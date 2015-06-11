package amtt.epam.com.amtt.api.http;

import org.apache.http.HttpEntity;

import java.util.Map;

import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 */
public class HttpRequestParams<ResultType> {

    private Map<String, String> mHeaders;
    private String mUrl;
    private Processor<ResultType, HttpEntity> mProcessor; //processor for retrieving OBJECTS
    private HttpEntity mPostEntity;

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

}
