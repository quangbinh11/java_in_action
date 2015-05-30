package org.lqbinh.httprequest2;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by lqbinh on 28/05/2015.
 */
public class HttpRequestBuilder {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    protected HttpRequestBuilder() {
        super();
    }

    public static HttpRequestBuilder create(){
        return new HttpRequestBuilder();
    }

    public HttpRequestBuilder maxTotalConnections(int max) {
        cm.setMaxTotal(max);

        return this;
    }

    public HttpRequestBuilder defaultMaxConnectionsPerRoute(int max) {
        cm.setDefaultMaxPerRoute(max);

        return this;
    }

    public HttpRequestBuilder maxTotalConnectionsPerRoute(String ip, int port, int max) {
        HttpHost host = new HttpHost(ip, port);
        cm.setMaxPerRoute(new HttpRoute(host), max);

        return this;
    }

    public CloseableHttpClient build(){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }


    public int getActiveConnections() {
        return cm.getTotalStats().getLeased();
    }

    public int getIdleConnections() {
        return cm.getTotalStats().getAvailable();
    }

    public int getRequestQueue() {
        return cm.getTotalStats().getPending();
    }

    public int getMaxConnections() {
        return cm.getTotalStats().getMax();
    }


}
