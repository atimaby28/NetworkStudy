import was.controller.v2.SearchControllerV2;
import was.controller.v2.SiteControllerV2;
import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServletV2;
import was.httpserver.servlet.annotation.AnnotationServletV3;

import java.io.IOException;
import java.util.List;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SiteControllerV2(), new SearchControllerV2());
        HttpServlet annotationServlet = new AnnotationServletV3(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet);
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}