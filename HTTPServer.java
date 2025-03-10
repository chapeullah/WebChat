import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.ServerSocket;

public class HTTPServer {

    private static final int PORT = 8080;
    private static final String IP = "localhost";
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private static volatile boolean running = true;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started on " + IP + ":" + PORT);

            new Thread(() -> {
            }).start();

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Client connected: " + socket.getInetAddress());

                    InputStream inputStream = socket.getInputStream();
                    while (true) {
                        int data = inputStream.read();
                        if (data == -1) {
                            System.out.println("Client disconnected: " + socket.getInetAddress());
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}