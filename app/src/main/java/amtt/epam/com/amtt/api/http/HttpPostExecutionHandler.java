package amtt.epam.com.amtt.api.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 */
public interface HttpPostExecutionHandler {

    <ResultType> HttpResult handleResponse(HttpResponse httpResponse,
                                           HttpRequestBase httpRequestBase,
                                           Processor<ResultType, HttpEntity> processor) throws AmttHttpException;

    AmttHttpException handleException(Exception e, HttpRequestBase httpRequestBase);

}
