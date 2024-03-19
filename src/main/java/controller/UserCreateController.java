package controller;

import db.DataBase;
import model.User;
import util.HttpRequest;
import util.HttpRequestUtils;
import util.HttpResponse;
import util.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class UserCreateController extends AbstractController {
    @Override
    public HttpResponse post(HttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        byte[] body;
        HttpStatus status;
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getBody());
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);

        headers.put("Location", "/index.html");
        body = "회원가입이 완료되었습니다.".getBytes();
        status = HttpStatus.HTTP_302_FOUND;
        return new HttpResponse(status, headers, body);
    }
}
