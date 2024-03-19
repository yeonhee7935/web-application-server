package controller;

import util.HttpMethod;
import util.HttpRequest;
import util.HttpResponse;

public abstract class AbstractController implements Controller {
    @Override
    public HttpResponse service(HttpRequest request) {
        if (request.getMethod().equals(HttpMethod.GET)) return get(request);
        else return post(request);
    }


    public HttpResponse post(HttpRequest request) {
        return null;
    }

    public HttpResponse get(HttpRequest request) {
        return null;
    }
}
