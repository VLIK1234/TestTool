package amtt.epam.com.amtt.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Present parameters which are passed to the HttpClient
 */
public class Request<ResultType> {

    public static class Builder<ResultType> {

        private Map<String, String> mHeaders;
        private String mUrl;
        private Processor<ResultType, HttpEntity> mProcessor;
        private HttpEntity mPostEntity;
        private HttpRequestBase mHttpRequestBase;

        public Builder setHeaders(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public Map<String, String> getHeaders() {
            return mHeaders;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public String getUrl() {
            return mUrl;
        }

        public Builder setProcessor(Processor<ResultType, HttpEntity> processor) {
            mProcessor = processor;
            return this;
        }

        public Processor getProcessor() {
            return mProcessor;
        }

        public Builder setPostEntity(String postEntityString) throws UnsupportedEncodingException {
            mPostEntity = new StringEntity(postEntityString);
            return this;
        }

        public Builder setPostEntity(ArrayList<String> filesPaths) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (int i = 0; i < filesPaths.size(); i++) {
                String file = filesPaths.get(i);
                File fileToUpload = new File(file);
                if (file.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
                    multipartEntityBuilder.addBinaryBody(MediaType.FILE_TYPE, fileToUpload, ContentType.create(MimeType.IMAGE_JPEG.getType()), fileToUpload.getName());
                } else if (file.contains(MimeType.TEXT_PLAIN.getFileExtension())) {
                    multipartEntityBuilder.addBinaryBody(MediaType.FILE_TYPE, fileToUpload, ContentType.create(MimeType.TEXT_PLAIN.getType()), fileToUpload.getName());
                }
            }
            mPostEntity = multipartEntityBuilder.build();
            return this;
        }

        public HttpEntity getPostEntity() {
            return mPostEntity;
        }

        public Builder setHttpRequestBase(HttpRequestBase httpRequestBase) {
            mHttpRequestBase = httpRequestBase;
            return this;
        }

        public HttpRequestBase getHttpRequestBase() {
            return mHttpRequestBase;
        }

        public Request create() {
            Request<ResultType> requestParams = new Request<>();
            requestParams.mProcessor = this.mProcessor;
            requestParams.mHttpRequestBase = this.mHttpRequestBase;
            return requestParams;
        }

    }

    private Processor<ResultType, HttpEntity> mProcessor;
    private HttpRequestBase mHttpRequestBase;

    public Processor<ResultType, HttpEntity> getProcessor() {
        return mProcessor;
    }

    public HttpRequestBase getHttpRequestBase() {
        return mHttpRequestBase;
    }

}
