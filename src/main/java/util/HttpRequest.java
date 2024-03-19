package util;

import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String uri;
    private String url;
    private Map<String, String> queryString;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private String body;

    public HttpRequest() {
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(HttpMethod method) {
        this.method =method;
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

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
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

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getBody() {
        return body;
    }
}
