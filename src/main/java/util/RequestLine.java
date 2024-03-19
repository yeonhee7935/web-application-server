package util;

import java.util.Map;

public class RequestLine{
    private final HttpMethod method;
    private final String uri;
    private final String url;
    private final Map<String, String> queryString;
    public RequestLine(String requestLine){
        if (requestLine == null || requestLine.isEmpty()) throw new RuntimeException("first line should not empty!");
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) throw new RuntimeException("first line should be like (method url version)!");
        
        
        HttpMethod method = HttpMethod.valueOf(tokens[0].toUpperCase());
        String uri = tokens[1];
        int index = uri.indexOf("?");
        String url = uri.contains("?") ? uri.substring(0, index) : uri;
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(uri.substring(index + 1));
        
        
        this.method= method;
        this.uri = uri;
        this.url = url;
        this.queryString = queryString;
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
}
