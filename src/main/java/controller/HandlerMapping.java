package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Map<String, Controller> controllers = new HashMap<>();

    public HandlerMapping() {
        controllers.put("/user/login", new UserLoginController());
        controllers.put("/user/create", new UserCreateController());
        controllers.put("/user/list", new UserListController());
        controllers.put("*", new DefaultController());
    }

    public Controller getController(String path) {
        Controller defaultController = this.controllers.get("*");
        return controllers.getOrDefault(path, defaultController);
    }

}
