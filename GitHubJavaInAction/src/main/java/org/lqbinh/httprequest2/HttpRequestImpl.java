package org.lqbinh.httprequest2;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by lqbinh on 29/05/2015.
 */
public class HttpRequestImpl implements OcrRequest {

    CloseableHttpClient _httpClient;
    CredentialsProvider credsProvider = new BasicCredentialsProvider();


    private Integer _defaultMaxConnectionsPerRoute;
    private Integer _maxTotalConnections;
    private String _ip;
    private Integer _port;
    private Integer _max;

    private HttpRequestImpl(Builder builder) {
        _httpClient = builder._httpClient;
        _defaultMaxConnectionsPerRoute = builder._defaultMaxConnectionsPerRoute;
        _maxTotalConnections = builder._maxTotalConnections;
        _ip = builder._ip;
        _port = builder._port;
        _max = builder._max;
    }

    public static class Builder {
        private CloseableHttpClient _httpClient;
        private Integer _defaultMaxConnectionsPerRoute;
        private Integer _maxTotalConnections;
        private String _ip;
        private Integer _port;
        private Integer _max;


        public Builder defaultMaxConnectionsPerRoute(Integer defaultMaxConnectionsPerRoute) {
            _defaultMaxConnectionsPerRoute = defaultMaxConnectionsPerRoute;
            return this;
        }

        public Builder maxTotalConnections(Integer maxTotalConnections) {
            _maxTotalConnections = maxTotalConnections;
            return this;
        }

        public Builder maxTotalConnectionsPerRoute(String ip, Integer port, Integer max) {
            _ip = ip;
            _port = port;
            _max = max;

            return this;
        }


        public HttpRequestImpl build() {

            HttpRequestBuilder builder = HttpRequestBuilder.create();

            if (_defaultMaxConnectionsPerRoute != null) {
                builder.defaultMaxConnectionsPerRoute(_defaultMaxConnectionsPerRoute);
            }

            if (_maxTotalConnections != null) {
                builder.maxTotalConnections(_maxTotalConnections);
            }

            if (_ip != null && _port != null && _max != null) {
                builder.maxTotalConnectionsPerRoute(_ip, _port, _max);
            }

            _httpClient = builder.build();

            return new HttpRequestImpl(this);
        }


    }

    public static HttpRequestImpl create() {
        return new Builder().build();
    }

    public static Builder custom() {
        return new Builder();
    }


    @Override
    public <T> T ocr(String url, String model) throws Exception {

        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("data", new Gson().toJson(model));

        httpPost.setEntity(builder.build());


        return request(httpPost);
    }

    private <T> T request(HttpUriRequest request)
            throws Exception {
        T t;
        try {
            t = (T) _httpClient.execute(request, stringResponseHandler());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return t;
    }

    public ResponseHandler<String> stringResponseHandler() {
        return new StringResponseHandler();
    }

    private static class StringResponseHandler implements
            ResponseHandler<String> {

        @Override
        public String handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException(
                        "Unexpected response status: " + status);
            }
        }
    }

}
