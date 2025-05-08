package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static util.MyLogger.log;

public class ReadHandler implements Runnable {

    private final DataInputStream inputStream;
    private final Client client;
    public boolean closed = false;

    public ReadHandler(DataInputStream inputStream, Client client) {
        this.inputStream = inputStream;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String received = inputStream.readUTF();
                System.out.println(received);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            client.close();
        }
    }

    public synchronized void close() {
        if(closed) return;

        // 종료 로직 필요시 작성
        closed = true;
        log("ReadHandler 종료");
    }
}
