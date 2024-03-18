package util;



import java.util.Map;

public class HttpResponse {
    private final Status status;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse(Status status, Map<String, String> headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
