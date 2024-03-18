package util;

import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private Map<String, String> queryString;
    private Map<String, String> headers;
    private String body;

    public HttpRequest() {
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setQueryString(Map<String, String> queryString) {
        this.queryString = queryString;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
