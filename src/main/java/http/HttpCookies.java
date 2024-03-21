package http;

import java.util.Map;

public class HttpCookies {
    private Map<String , String> cookies;

    public HttpCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getAll() {
        return cookies;
    }
}
