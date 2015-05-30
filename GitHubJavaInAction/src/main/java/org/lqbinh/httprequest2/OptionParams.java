package org.lqbinh.httprequest2;

/**
 * Created by quangbinh on 5/30/15.
 */
public class OptionParams {
    private boolean deskew;
    private String language;
    private long timeout;
    private boolean acceptFirstResult;
    private boolean includeCharacterResult;
    private String pattern;

    public void setDeskew(boolean deskew) {
        this.deskew = deskew;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setAcceptFirstResult(boolean acceptFirstResult) {
        this.acceptFirstResult = acceptFirstResult;
    }

    public void setIncludeCharacterResult(boolean includeCharacterResult) {
        this.includeCharacterResult = includeCharacterResult;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
