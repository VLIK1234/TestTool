package amtt.epam.com.amtt.api.http.postexecution;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import amtt.epam.com.amtt.api.http.HttpException;
import amtt.epam.com.amtt.api.http.HttpResult;
import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Describes HttpResponse handling after HttpRequest execution
 */
public interface HttpPostExecutionHandler {

    <ResultType> HttpResult handleResponse(HttpResponse httpResponse,
                                           HttpRequestBase httpRequestBase,
                                           Processor<ResultType, HttpEntity> processor) throws HttpException;

    HttpException handleException(Exception e, HttpRequestBase httpRequestBase);

}
