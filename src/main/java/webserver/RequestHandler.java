package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import controller.Controller;
import controller.HandlerMapping;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HandlerMapping handlerMapping;

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.handlerMapping = new HandlerMapping();
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 1. Parse Request
            HttpRequest request = HttpRequestUtils.parseRequest(in);

            // 2. Create Response
            Controller controller = this.handlerMapping.getController(request.getUrl());
            HttpResponse response = controller.service(request);

            // 3. Send Response
            HttpResponseUtils.sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
