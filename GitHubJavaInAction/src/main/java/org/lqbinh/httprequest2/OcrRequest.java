package org.lqbinh.httprequest2;

/**
 * Created by lqbinh on 29/05/2015.
 */
public interface OcrRequest {
    public <T> T ocr(String url, String model) throws Exception;

}
