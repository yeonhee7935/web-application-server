package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void sendResponse(OutputStream out, HttpResponse response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 " + response.getStatus().getCode() + " " + response.getStatus().getMessage() + " \r\n");
            if (!response.getHeaders().containsKey("Content-Type"))
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
            Map<String, String> headers = response.getHeaders();
            for (String key : headers.keySet()) dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(response.getBody());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
