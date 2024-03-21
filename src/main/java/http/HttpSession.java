package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private final Map<String, Object> attributes;
    public HttpSession() {
        attributes = new HashMap<>();
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }
    public void setAttribute(String key, Object value){
        attributes.put(key,value);
    }
}
