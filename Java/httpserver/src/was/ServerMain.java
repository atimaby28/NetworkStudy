package was;

import java.io.IOException;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        HttpServerV2 server = new HttpServerV2(PORT);

        server.start();
    }
}
