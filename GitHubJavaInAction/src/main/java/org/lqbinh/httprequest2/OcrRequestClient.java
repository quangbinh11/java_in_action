package org.lqbinh.httprequest2;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by lqbinh on 28/05/2015.
 */
public class OcrRequestClient {

    CloseableHttpClient _httpClient;
    private int _defaultMaxConnectionsPerRoute;
    private int _maxTotalConnections;
    private String _ip;
    private int _port;
    private int _max;

    private OcrRequestClient(Builder builder) {
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

        public Builder maxTotalConnectionsPerRoute(String ip, int port, int max) {
            _ip = ip;
            _port = port;
            _max = max;

            return this;
        }


        public OcrRequestClient build() {

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

            return new OcrRequestClient(this);
        }


    }

    public static OcrRequestClient defaultClient() {
        return new Builder().defaultMaxConnectionsPerRoute(1).maxTotalConnections(1).build();
    }

    public static OcrRequestClient defaultServer() {
        return new Builder().defaultMaxConnectionsPerRoute(20).maxTotalConnections(20).build();
    }

    public static Builder custom() {
        return new Builder();
    }

    public <T> T post(String url, HttpEntity entity, ResponseHandler<T> handler)
            throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (entity != null) {
            httpPost.setEntity(entity);
        }

        return request(httpPost, handler);
    }

    public <T> T put(String url, HttpEntity entity, ResponseHandler<T> handler)
            throws Exception {
        HttpPut httpPut = new HttpPut(url);
        if (entity != null) {
            httpPut.setEntity(entity);
        }

        return request(httpPut, handler);
    }

    private <T> T request(HttpUriRequest request, ResponseHandler<T> handler)
            throws Exception {
        T t;
        try {
            t = _httpClient.execute(request, handler);
        } catch (Exception e) {
            System.out.println("ALL RETRY ATTEMPT FAIL");
            throw e;
        }
        return t;
    }

}
