package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 1. Parse Request Line
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            String[] tokens = HttpRequestUtils.parseRequestLine(line);
            String method = tokens[0];
            String url = tokens[1];

            // 2. Parse Request Headers
            Map<String, String> headers = new HashMap<>();
            while (!line.isEmpty()) {
                line = br.readLine();
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                if (pair != null) headers.put(pair.getKey(), pair.getValue());
            }
            int contentLength = headers.containsKey("Content-Length") ? Integer.parseInt(headers.get("Content-Length")) : 0;

            // 3. Parse Request Body
            DataOutputStream dos = new DataOutputStream(out);
            String body = IOUtils.readData(br, contentLength);

            Status httpStatus = null;
            byte[] responseBody = null;
            Map<String, String> responseHeaders = new HashMap<>();

            // 4. Send Response
            if (url.startsWith("/user/create")) {
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                DataBase.addUser(user);

                responseHeaders.put("Location", "/index.html");
                responseBody = "회원가입이 완료되었습니다.".getBytes();
                httpStatus = Status.HTTP_302_FOUND;
            } else if (url.equals("/user/login")) {
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = DataBase.findUserById(params.get("userId"));
                if (user == null) {
                    responseHeaders.put("Location", "/user/login_failed.html");
                    responseBody = "로그인 실패".getBytes();
                    httpStatus = Status.HTTP_302_FOUND;
                } else {
                    boolean logined = user.getPassword().equals(params.get("password"));
                    responseHeaders.put("Set-Cookie", "logined=" + logined);
                    if (logined) {
                        responseHeaders.put("Location", "/index.html");
                        responseBody = "로그인 성공".getBytes();
                        httpStatus = Status.HTTP_302_FOUND;
                    } else {
                        responseHeaders.put("Location", "/user/login_failed.html");
                        responseBody = "로그인 실패".getBytes();
                        httpStatus = Status.HTTP_302_FOUND;
                    }
                }
            } else if (url.equals("/user/list")) {
                Map<String, String> cookies = HttpRequestUtils.parseCookies(headers.get("Cookie"));
                if (cookies.containsKey("logined") && Boolean.parseBoolean(cookies.get("logined"))) {
                    Collection<User> users = DataBase.findAll();
                    responseHeaders.put("Content-Type", "text/html; charset=utf-8");
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
                    responseBody = sb.toString().getBytes();
                    httpStatus = Status.HTTP_200_OK;

                } else {
                    responseHeaders.put("Location", "/user/login.html");
                    responseBody = "로그인이필요합니다.".getBytes();
                    httpStatus = Status.HTTP_302_FOUND;
                }

            } else {
                responseBody = Files.readAllBytes(new File("./webapp" + url).toPath());
                httpStatus = Status.HTTP_200_OK;

            }
            HttpResponseUtils.sendResponse(dos, new HttpResponse(httpStatus, responseHeaders, responseBody));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
