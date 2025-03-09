import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class HTTPServer {

    private static int PORT = 8080;
    private static String IP = "localhost";
    private static Socket clientSocket;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("Server socket: " + IP + ":" + PORT);

            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}