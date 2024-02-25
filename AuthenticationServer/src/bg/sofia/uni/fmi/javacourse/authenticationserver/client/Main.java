package bg.sofia.uni.fmi.javacourse.authenticationserver.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Main {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            System.out.println("Connected to the server.");
            while (true) {
                System.out.print("CMD: ");
                String message = scanner.nextLine(); // read a line from the console
                if ("quit".equals(message)) {
                    break;
                }

                buffer.clear();
                buffer.put(message.getBytes());
                buffer.putChar('\0');
                buffer.flip();
                socketChannel.write(buffer);

                buffer.clear();
                socketChannel.read(buffer);
                buffer.flip();

                int reply = buffer.getInt();
                System.out.println("Response: " + reply);
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
//register --username m --password 123 --first-name m --last-name m --email m