package was;

import java.io.IOException;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        HttpServerV3 server = new HttpServerV3(PORT);

        server.start();
    }
}
