package org.lqbinh.httprequest;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by lqbinh on 29/05/2015.
 */
public class HttpClientImpl implements HttpClient {

    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm).build();

    @Override
    public String post(String url,  String model) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("data", new Gson().toJson(model));

        httpPost.setEntity(builder.build());


        return request(httpPost);
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

    public <T> T request(HttpUriRequest request)
            throws Exception {
        T t = null;
        try {
            t = (T)httpClient.execute(request, stringResponseHandler());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return t;
    }
}
