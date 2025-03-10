import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class HTTPServer {

    private static int PORT = 8080;
    private static String IP = "localhost";
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on " + IP + ":" + PORT);

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
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}