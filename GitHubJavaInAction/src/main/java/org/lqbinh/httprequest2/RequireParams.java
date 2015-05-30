package org.lqbinh.httprequest2;

/**
 * Created by quangbinh on 5/30/15.
 */
public class RequireParams {

    private String ocrType;
    private String pattern;
    private String imageUrl;

    public RequireParams(String ocrType, String pattern, String imageUrl) {
        this.ocrType = ocrType;
        this.pattern = pattern;
        this.imageUrl = imageUrl;

    }
}
