package util;



import java.util.Map;

public class HttpResponse {
    private final HttpStatus httpStatus;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse(HttpStatus httpStatus, Map<String, String> headers, byte[] body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
