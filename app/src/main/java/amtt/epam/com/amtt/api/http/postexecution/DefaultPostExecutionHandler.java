package amtt.epam.com.amtt.api.http.postexecution;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import amtt.epam.com.amtt.api.http.HttpClient;
import amtt.epam.com.amtt.api.http.HttpException;
import amtt.epam.com.amtt.api.http.HttpResult;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Default HttpPostExecutionHandler implementation that defines HttpResponse processing logic
 */
public class DefaultPostExecutionHandler implements HttpPostExecutionHandler {

    private final String TAG = getClass().getSimpleName();

    @Override
    public <ResultType> HttpResult handleResponse(HttpResponse httpResponse,
                                                  HttpRequestBase httpRequestBase,
                                                  Processor<ResultType, HttpEntity> processor) throws HttpException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (httpRequestBase.getMethod().equals(HttpGet.METHOD_NAME) && statusCode != HttpStatus.SC_OK ||
                httpRequestBase.getMethod().equals(HttpPost.METHOD_NAME) && statusCode != HttpStatus.SC_CREATED) {
            throw new HttpException(null, statusCode, httpRequestBase, null);
        }

        ResultType result = null;
        HttpEntity entity = null;
        try {
            if (processor != null) {
                entity = httpResponse.getEntity();
                result = processor.process(entity);
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
            throw prepareException(e, statusCode, entity, httpRequestBase);
        }

        return new HttpResult<>(httpRequestBase.getMethod(), result);
    }

    @Override
    public HttpException handleException(Exception e, HttpRequestBase httpRequestBase) {
        return new HttpException(e, HttpClient.EMPTY_STATUS_CODE, httpRequestBase, null);
    }

    private HttpException prepareException(Exception e, int statusCode, HttpEntity entity, HttpRequestBase httpRequestBase) {
        String entityString = null;
        try {
            entityString = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (IOException entityParseException) {
            Logger.e(TAG, e.getMessage());
            e.addSuppressed(e);
        }
        return new HttpException(e, statusCode, httpRequestBase, entityString);
    }

}
