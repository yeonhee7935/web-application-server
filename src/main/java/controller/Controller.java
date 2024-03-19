package controller;

import util.HttpRequest;
import util.HttpResponse;

public interface Controller {
    public HttpResponse service(HttpRequest request );
}
