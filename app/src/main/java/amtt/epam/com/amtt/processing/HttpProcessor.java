package amtt.epam.com.amtt.processing;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.http.HttpResult;
import amtt.epam.com.amtt.http.Request;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 * Processor that get response after sending request to one of the Project tracking system / Bug tracking system (like Jira),
 * i.e. processing api responses
 */
@SuppressWarnings("unchecked")
public class HttpProcessor<ResultType> implements Processor<HttpResult<ResultType>, HttpResponse> {

    private final String TAG = getClass().getSimpleName();

    private final Request<ResultType> mRequest;

    public HttpProcessor(Request request) {
        mRequest = request;
    }

    @Override
    public HttpResult<ResultType> process(HttpResponse httpResponse) throws Exception {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String requestMethodName = mRequest.getHttpRequestBase().getMethod();

        if (requestMethodName.equals(HttpGet.METHOD_NAME) && statusCode != HttpStatus.SC_OK ||
                requestMethodName.equals(HttpPost.METHOD_NAME) && statusCode != HttpStatus.SC_CREATED) {
            throw new HttpException(null, statusCode, mRequest, null);
        }

        ResultType result = null;
        HttpEntity entity = null;
        try {
            if (mRequest.getProcessor() != null) {
                entity = httpResponse.getEntity();
                result = mRequest.getProcessor().process(entity);
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
            throw prepareException(e, statusCode, entity);
        }

        return new HttpResult<>(requestMethodName, result);
    }

    private HttpException prepareException(Exception e, int statusCode, HttpEntity entity) {
        String entityString = null;
        try {
            entityString = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (IOException entityParseException) {
            Logger.e(TAG, e.getMessage());
            e.addSuppressed(e);
        }
        return new HttpException(e, statusCode, mRequest, entityString);
    }

}
