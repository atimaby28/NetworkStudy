package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static util.MyLogger.log;
import static util.SocketCloseUtil.closeAll;

public class Client {

    private final String host;
    private final int port;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private ReadHandler readHandler;
    private WriteHandler writeHandler;
    private boolean closed = false;


    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        log("클라이언트 시작...");

        socket = new Socket(host, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        readHandler = new ReadHandler(inputStream, this);
        writeHandler = new WriteHandler(outputStream, this);

        Thread readThread = new Thread(readHandler, "ReadHandler");
        Thread writeThread = new Thread(writeHandler, "WriteHandler");

        readThread.start();
        writeThread.start();
    }

    public synchronized void close() {
        if(closed) return;

        writeHandler.close();
        readHandler.close();

        closeAll(socket, inputStream, outputStream);
        closed = true;

        log("연결 종료: " + socket);
    }
}
