package was.controller;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

public class SiteController {

    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
