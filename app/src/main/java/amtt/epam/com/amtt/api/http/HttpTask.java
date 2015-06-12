package amtt.epam.com.amtt.api.http;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

import amtt.epam.com.amtt.api.http.postexecution.HttpPostExecutionHandler;
import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Executes http requests asynchronously
 */
public class HttpTask<ResultType> extends AsyncTask<Void, Void, HttpResult> {

    public static interface HttpCallback {

        void onTaskStart();

        void onTaskExecuted(HttpResult httpResult);

        void onTaskError(HttpException e);

    }

    public static class Builder<ResultType> {

        private HttpCallback mCallback;
        private HttpRequestBase mHttpRequestBase;
        private HttpPostExecutionHandler mPostExecutionHandler;
        private Processor<ResultType, HttpEntity> mProcessor;

        public Builder setProcessor(Processor<ResultType, HttpEntity> processor) {
            mProcessor = processor;
            return this;
        }

        public Builder setHttpRequest(HttpRequestBase httpRequestBase) {
            mHttpRequestBase = httpRequestBase;
            return this;
        }

        public Builder setPostExecutionHandler(HttpPostExecutionHandler postExecutionHandler) {
            mPostExecutionHandler = postExecutionHandler;
            return this;
        }

        public Builder setCallback(HttpCallback callback) {
            mCallback = callback;
            return this;
        }

        public void createAndExecute() {
            HttpTask httpTask = new HttpTask();
            httpTask.mHttpRequestBase = this.mHttpRequestBase;
            httpTask.mPostExecutionHandler = this.mPostExecutionHandler;
            httpTask.mCallback = this.mCallback;
            httpTask.mProcessor = this.mProcessor;
            httpTask.execute();
        }

    }

    private HttpRequestBase mHttpRequestBase;
    private HttpPostExecutionHandler mPostExecutionHandler;
    private HttpCallback mCallback;
    private HttpException mException;
    private Processor<ResultType, HttpEntity> mProcessor;

    private static final org.apache.http.client.HttpClient sHttpClient;

    static {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 8000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        sHttpClient = new DefaultHttpClient(httpParameters);
    }

    public HttpTask() {
        super();
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            mCallback.onTaskStart();
        }
    }

    @Override
    protected HttpResult doInBackground(Void... params) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = sHttpClient.execute(mHttpRequestBase);
        } catch (IOException e) {
            mException = mPostExecutionHandler.handleException(e, mHttpRequestBase);
        }
        HttpResult httpResult = null;
        try {
            httpResult = mPostExecutionHandler.handleResponse(httpResponse, mHttpRequestBase, mProcessor);
        } catch (HttpException e) {
            mException = e;
        }
        return httpResult;
    }

    @Override
    protected void onPostExecute(HttpResult httpResult) {
        if (mCallback != null) {
            if (httpResult != null) {
                mCallback.onTaskExecuted(httpResult);
            } else {
                mCallback.onTaskError(mException);
            }
        }
    }
}
