package org.lqbinh.httprequest;

import java.util.Map;

/**
 * Created by lqbinh on 29/05/2015.
 */
public interface OcrClient {
    String ocr(String url, String base64);

    String ocr(String url, Map<String, String> params);
}
