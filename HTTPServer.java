import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            System.out.println(timeNow() + " Server started on " + IP + ":" + PORT);

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    String command = reader.readLine();
                    if ("help".equalsIgnoreCase(command)) {
                        System.out.println(timeNow() + " Commands: \n- shutdown");
                    } else if ("shutdown".equalsIgnoreCase(command)) stopServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (running) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String requestLine;
                    System.out.println(timeNow() + " === RECEIVED REQUEST FROM " + socket.getInetAddress() + " ===");
                    while ((requestLine = reader.readLine()) != null && !requestLine.isEmpty()) System.out.println(timeNow() + " " + requestLine);
                    System.out.println(timeNow() + " === END OF REQUEST " + socket.getInetAddress() + " ===");

                    String responseBody = "<html><body><h1>Server's response</h1></body></html>";
                    writer.write("HTTP/1.1 200 OKr\n");
                    writer.write("Content-Type: text/html; charset=UTF-8\r\n");
                    writer.write("Content-Length: " + responseBody.length() + "\r\n");
                    writer.write("Connection: close\r\n");
                    writer.write("\r\n");
                    writer.write(responseBody);
                    try {
                        writer.flush();
                    } catch (IOException e) {
                        System.out.println(timeNow() + "Client closed connection before receiving response.");
                    }
                } catch (IOException e) {
                    if (!running) break;
                    e.printStackTrace(); 
                } finally {
                    try {
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void stopServer() {
        running = false;
        try {
            serverSocket.close();
            executor.shutdown();
            System.out.println("Server stopped!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String timeNow () {
        return "[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "]";
    }
}