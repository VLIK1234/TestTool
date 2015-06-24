package amtt.epam.com.amtt.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Present parameters which are passed to the HttpClient
 */
public class Request {

    public enum Type {

        GET,
        POST,
        DELETE

    }

    public static class Builder implements amtt.epam.com.amtt.common.Builder<Request> {

        private Map<String, String> mHeaders;
        private String mUrl;
        private String mProcessorName;
        private HttpEntity mEntity;
        private Type mType;

        public Builder setHeaders(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public String getUrl() {
            return mUrl;
        }

        public Builder setProcessorName(String processorName) {
            mProcessorName = processorName;
            return this;
        }

        public Builder setEntity(HttpEntity entity) {
            mEntity = entity;
            return this;
        }

        public Builder setEntity(ArrayList<String> filesPaths) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (int i = 0; i < filesPaths.size(); i++) {
                String file = filesPaths.get(i);
                File fileToUpload = new File(file);
                if (file.contains(MimeType.IMAGE_PNG.getFileExtension())) {
                    multipartEntityBuilder.addBinaryBody(MediaType.FILE_TYPE, fileToUpload, ContentType.create(MimeType.IMAGE_PNG.getType()), fileToUpload.getName());
                } else if (file.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
                    multipartEntityBuilder.addBinaryBody(MediaType.FILE_TYPE, fileToUpload, ContentType.create(MimeType.IMAGE_JPEG.getType()), fileToUpload.getName());
                } else if (file.contains(MimeType.TEXT_PLAIN.getFileExtension())) {
                    multipartEntityBuilder.addBinaryBody(MediaType.FILE_TYPE, fileToUpload, ContentType.create(MimeType.TEXT_PLAIN.getType()), fileToUpload.getName());
                }
            }
            mEntity = multipartEntityBuilder.build();
            return this;
        }

        public Builder setType(Type type) {
            mType = type;
            return this;
        }

        public Type getType() {
            return mType;
        }

        @Override
        public Request build() {
            Request requestParams = new Request();
            requestParams.mHeaders = this.mHeaders;
            requestParams.mUrl = this.mUrl;
            requestParams.mProcessorName = this.mProcessorName;
            requestParams.mEntity = this.mEntity;
            requestParams.mType = this.mType;
            return requestParams;
        }

    }

    private Map<String, String> mHeaders;
    private String mUrl;
    private String mProcessorName;
    private HttpEntity mEntity;
    private HttpRequestBase mHttpRequestBase;
    private Type mType;

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public String getProcessorName() {
        return mProcessorName;
    }

    public HttpEntity getEntity() {
        return mEntity;
    }

    public HttpRequestBase getHttpRequestBase() {
        return mHttpRequestBase;
    }

    public void setHttpRequestBase(HttpRequestBase request) {
        mHttpRequestBase = request;
    }

    public Type getType() {
        return mType;
    }

    public String getUrl() {
        return mUrl;
    }

}
