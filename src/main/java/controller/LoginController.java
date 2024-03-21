package controller;

import http.HttpSession;
import model.User;
import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;

public class LoginController extends AbstractController {
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        HttpSession session = request.getSession();
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user != null) {
            if (user.login(request.getParameter("password"))) {
                session.setAttribute("user", user);
                response.sendRedirect("/index.html");
            } else {
                response.sendRedirect("/user/login_failed.html");
            }
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
