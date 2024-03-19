package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpRequestUtils;
import util.HttpResponse;
import util.HttpStatus;
import webserver.RequestHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserListController extends AbstractController{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    @Override
    public HttpResponse get(HttpRequest request){
        Map<String, String> headers = new HashMap<>();
        byte[] body;
        HttpStatus status;
        Map<String, String> cookies = request.getCookies();
        if (cookies.containsKey("logined") && Boolean.parseBoolean(cookies.get("logined"))) {
            Collection<User> users = DataBase.findAll();
            headers.put("Content-Type", "text/html; charset=utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("<thead><tr>\n<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th></tr></thead>");
            sb.append("<tbody>");
            for (User user : users) {
                sb.append("<tr>");
                sb.append("<th>").append(user.getUserId()).append("</th>");
                sb.append("<td>").append(user.getName()).append("</td>");
                sb.append("<td>").append(user.getEmail()).append("</td>");
                sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
                sb.append("</tr>");
            }
            sb.append("</tbody></table>");
            body = sb.toString().getBytes();
            status = HttpStatus.HTTP_200_OK;

        } else {
            headers.put("Location", "/user/login.html");
            body = "로그인이필요합니다.".getBytes();
            status = HttpStatus.HTTP_302_FOUND;
        }

        return  new HttpResponse(status, headers, body);
    }
}
