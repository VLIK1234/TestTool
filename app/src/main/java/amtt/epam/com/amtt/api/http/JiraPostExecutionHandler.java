package amtt.epam.com.amtt.api.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 */
public class JiraPostExecutionHandler implements HttpPostExecutionHandler {

    public static class JiraException extends AmttHttpException {

        public JiraException(Exception suppressedException, int resultCode, HttpRequestBase mHttpRequest, String entityString) {
            super(suppressedException, resultCode, mHttpRequest, entityString);
        }

    }

    private final String TAG = getClass().getSimpleName();

    @Override
    public <ResultType> HttpResult handleResponse(HttpResponse httpResponse,
                                                  HttpRequestBase httpRequestBase,
                                                  Processor<ResultType, HttpEntity> processor) throws AmttHttpException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        //TODO CAST TO THIS OUTWARD APPEARANCE
//        if (mRestMethodType == RestMethodType.GET && statusCode != HttpStatus.SC_OK ||
//                mRestMethodType == RestMethodType.POST && statusCode != HttpStatus.SC_CREATED) {
//            throw new AmttException(null, statusCode, this, null);
//        }

        if (statusCode != HttpStatus.SC_OK) {
            throw new JiraException(null, statusCode, httpRequestBase, null);
        }

        HttpResult<ResultType> httpResult = new HttpResult<>();
        ResultType result;
        HttpEntity entity = null;
        try {
            if (processor != null) {
                entity = httpResponse.getEntity();
                result = processor.process(entity);
                httpResult.setResultObject(result);
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
            throw prepareException(e, statusCode, entity, httpRequestBase);
        }

        return httpResult;
    }

    @Override
    public AmttHttpException handleException(Exception e, HttpRequestBase httpRequestBase) {
        return new JiraException(e, HttpClient.EMPTY_STATUS_CODE, httpRequestBase, null);
    }

    private JiraException prepareException(Exception e, int statusCode, HttpEntity entity, HttpRequestBase httpRequestBase) {
        String entityString = null;
        try {
            entityString = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (IOException entityParseException) {
            Logger.e(TAG, e.getMessage());
            e.addSuppressed(e);
        }
        return new JiraException(e, statusCode, httpRequestBase, entityString);
    }

}
