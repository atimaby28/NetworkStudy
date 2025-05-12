import was.controller.SearchController;
import was.controller.SiteController;
import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.httpserver.reflection.ReflectionServlet;
import was.httpserver.servlet.DiscardServlet;
import was.servlet.HomeServlet;

import java.io.IOException;
import java.util.List;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SiteController(), new SearchController());
        ReflectionServlet reflectionServlet = new ReflectionServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/", new HomeServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
