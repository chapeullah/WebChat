import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class HTTPServer {

    private static int PORT = 8080;
    private static String IP = "localhost";
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static InputStream inputStream;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server socket: " + IP + ":" + PORT);

            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            inputStream = clientSocket.getInputStream();
            while (true) {
                int data = inputStream.read();
                System.out.println("Check-----");
                if (data == -1) {
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Thread was failed: " + e.getMessage());
                    return;
                }
            }
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}