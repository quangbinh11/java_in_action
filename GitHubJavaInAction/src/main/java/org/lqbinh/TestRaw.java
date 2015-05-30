package org.lqbinh;

import com.google.gson.Gson;
import com.sps.vn.httprequest2.OptionParams;
import com.sps.vn.httprequest2.RequireParams;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lqbinh
 */
public class TestRaw {

    public static void main(String[] args) throws IOException, InterruptedException {

        File file = new File("C:\\httprequest2.tiff");

        InputStream inputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            inputStream = new FileInputStream(file);
            inputStream.read(bFile);
            inputStream.close();

            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String base64 = Base64.encodeBase64String(read(file));

//        OcrImageModel model = new OcrImageModel.Builder(OcrImageModel.OcrType.Transym.name(), base64, "http://fileservice/3da2f").build();


        CloseableHttpClient httpclient = HttpClients.createDefault();

        CredentialsProvider credsProvider =  new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("10.10.15.141", AuthScope.ANY_PORT),
                new UsernamePasswordCredentials("nctoan", "123456"));
        Lookup<AuthSchemeProvider> authRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
                .build();
        AuthCache authCache=new BasicAuthCache();

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthSchemeRegistry(authRegistry);
        context.setAuthCache(authCache);
        HttpPost httpPost = new HttpPost("http://10.10.15.141:8080/httprequest2-proxy/httprequest2/ocr_image");

//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addTextBody("data", new Gson().toJson(model));

        List<Object> models = new ArrayList<Object>();

        RequireParams require = new RequireParams("Transym", base64, "http://fileservice/3da2f");
        OptionParams option = new OptionParams();

        models.add(require);
        models.add(option);


        String content = new Gson().toJson(models);
        System.out.println(content);



        httpPost.setEntity(new StringEntity(content));
        httpPost.addHeader(new BasicHeader("Accept", "application/json"));
        httpPost.setHeader(HTTP.CONTENT_TYPE,"application/json");


        CloseableHttpResponse response1 = httpclient.execute(httpPost, context);


        try {
//            String result = ocrRequest.httprequest2("http://10.10.15.141:8080/httprequest2-proxy/httprequest2/ocr_image", model);

            System.out.println(EntityUtils.toString(response1.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static byte[] read(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
            }

            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }
}
