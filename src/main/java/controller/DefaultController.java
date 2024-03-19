package controller;

import util.HttpRequest;
import util.HttpResponse;
import util.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class DefaultController extends AbstractController {
    @Override
    public HttpResponse get(HttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        byte[] body;
        HttpStatus status;
        if (request.getUrl().endsWith(".css")) headers.put("Content-Type", "text/css");
        try {
            body = Files.readAllBytes(new File("./webapp" + request.getUrl()).toPath());
            status = HttpStatus.HTTP_200_OK;
            return new HttpResponse(status, headers, body);
        } catch (IOException e) {
            body = "리소스를 찾을 수 없습니다.".getBytes();
            status = HttpStatus.HTTP_404_NOT_FOUND;
            return new HttpResponse(status, headers, body);
        }
    }
}
