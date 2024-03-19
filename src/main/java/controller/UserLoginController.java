package controller;

import db.DataBase;
import model.User;
import util.HttpRequest;
import util.HttpRequestUtils;
import util.HttpResponse;
import util.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class UserLoginController extends AbstractController{
    @Override
    public HttpResponse post(HttpRequest request ) {
        Map<String, String> responseHeaders = new HashMap<>();
        byte[] responseBody;
        HttpStatus httpStatus;
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getBody());
        User user = DataBase.findUserById(params.get("userId"));
        if (user == null) {
            responseHeaders.put("Location", "/user/login_failed.html");
            responseBody = "로그인 실패".getBytes();
            httpStatus = HttpStatus.HTTP_302_FOUND;
        } else {
            boolean logined = user.getPassword().equals(params.get("password"));
            responseHeaders.put("Set-Cookie", "logined=" + logined);
            if (logined) {
                responseHeaders.put("Location", "/index.html");
                responseBody = "로그인 성공".getBytes();
                httpStatus = HttpStatus.HTTP_302_FOUND;
            } else {
                responseHeaders.put("Location", "/user/login_failed.html");
                responseBody = "로그인 실패".getBytes();
                httpStatus = HttpStatus.HTTP_302_FOUND;
            }
        }
        return  new HttpResponse(httpStatus, responseHeaders, responseBody);
    }
}
