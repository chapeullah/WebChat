import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.ServerSocket;

public class HTTPServer {

    private static final int PORT = 8080;
    private static final String IP = "localhost";
    private static ServerSocket serverSocket;
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static volatile boolean running = true;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on " + IP + ":" + PORT);

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    String command = reader.readLine();
                    if ("shutdown".equalsIgnoreCase(command)) {
                        running = false;
                        serverSocket.close();
                        executor.shutdown();
                        System.out.println("Server stopped.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (running) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    System.out.println("Client connected: " + socket.getInetAddress());

                    InputStream inputStream = socket.getInputStream();
                    while (true) {
                        int data = inputStream.read();
                        if (data == -1) {
                            System.out.println("Client disconnected: " + socket.getInetAddress());
                            break;
                        }
                    }
                } catch (IOException e) {
                    if (!running) break;
                    e.printStackTrace(); 
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}