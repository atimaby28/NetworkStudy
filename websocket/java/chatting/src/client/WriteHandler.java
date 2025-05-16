package client;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static util.MyLogger.log;

public class WriteHandler implements Runnable {

    private static final String DELIMITER = " | ";

    private final DataOutputStream outputStream;
    private final Client client;

    private boolean closed = false;

    public WriteHandler(DataOutputStream outputStream, Client client) {
        this.outputStream = outputStream;
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            String username = inputUsername(scanner);
            outputStream.writeUTF("/join" + DELIMITER + username);

            while (true) {
                String toSend = scanner.nextLine();

                if(toSend.isEmpty()) continue;

                if(toSend.equals("/exit")) {
                    outputStream.writeUTF(toSend);
                    break;
                }

                // "/"로 시작하면 명령어, 나머지는 일반 메시지
                if(toSend.startsWith("/")) {
                    outputStream.writeUTF(toSend);
                } else {
                    outputStream.writeUTF(username + "/message" + DELIMITER + toSend);
                }
            }
        } catch (IOException | NoSuchElementException e) {
            log(e);
        } finally {
            client.close();
        }
    }

    private static String inputUsername(Scanner scanner) throws IOException {
        System.out.println("이름을 입력하세요...");

        String username;

        do {
            username = scanner.nextLine();
        } while (username.isEmpty());

        return username;
    }

    public synchronized void close() {
        if(closed) return;

        try {
            System.in.close(); // Scanner 입력 중지 (사용자의 입력을 닫음)
        } catch (IOException e) {
            log(e);
        }

        closed = true;
        log("WriteHandler 종료");
    }
}
