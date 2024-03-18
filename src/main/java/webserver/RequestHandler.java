package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 1. Parse Request Headers
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            if (line == null || line.isEmpty()) throw new RuntimeException("first line should not empty!");
            String[] tokens = line.split(" ");
            if (tokens.length != 3) throw new RuntimeException("first line should be like (method url version)!");
            String url = tokens[1];
            int contentLength = 0;
            while (!line.isEmpty()) {
                line = br.readLine();
                if(line.contains("Content-Length")){
                    contentLength = getContentLength(line);
                }
            }
            // 2.
            byte[] responseBody = null;
            if(url.startsWith("/user/create")){
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = new User(params.get("userId"),params.get("password"),params.get("name"),params.get("email"));
                DataBase.addUser(user);
                responseBody = "회원가입이 완료되었습니다.".getBytes();
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
                responseBody(dos, responseBody);
            }else{
                responseBody = Files.readAllBytes(new File("./webapp"+url).toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, responseBody.length);
                responseBody(dos, responseBody);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private int getContentLength(String line){
        String[] tokens = line.split(":");
        return  Integer.parseInt(tokens[1].trim());
    }
    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\n \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
