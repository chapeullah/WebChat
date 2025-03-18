import java.io.*;
import java.net.Socket;
import java.nio.file.*;
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
    private static final String WEB_ROOT = "www";

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
                    else System.out.println("Unknown command.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (running) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    clientHandler(socket);
                } catch (IOException e) {
                    if (!running) break;
                    e.printStackTrace(); 
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                            System.out.println("SOCKET " + socket.getInetAddress() + " CLOSED");
                        }
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

    private static void clientHandler(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream();
            
            String requestLine = reader.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) return;

            String path = requestParts[1];
            System.out.println(timeNow() + " REQUEST FROM " + socket.getInetAddress() + " " + requestLine);
            if (path.equals("/")) path = "/index.html";
            Path filePath = Paths.get(WEB_ROOT + path);

            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                byte[] contentByte = Files.readAllBytes(filePath);
                sendResponse(output, "200 OK", getContentType(path), contentByte);
            } else {
                String notFound = "<html><body><h1>404 Not Found</h1></body></html>";
                sendResponse(output, "404 Not Found", "text/html", notFound.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void stopServer() {
        running = false;
        try {
            System.out.println(timeNow() + " Server stopped!");
            serverSocket.close();
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(OutputStream output, String status, String contentType, byte[] contentByte) throws IOException {
        PrintWriter writer = new PrintWriter(output);
        writer.write("HTTP/1.1 " + status + "\r\n");
        writer.write("Content-Type: " + contentType + "\r\n");
        writer.write("Content-Length: " + contentByte.length + "\r\n");
        writer.write("Connection: close\r\n");
        writer.write("\r\n");
        writer.flush();
        output.write(contentByte);
        output.flush();
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        else if (path.endsWith(".css")) return "text/css";
        else if (path.endsWith(".js")) return "application/javascript";
        else if (path.endsWith(".png")) return "image/png";
        else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        else if (path.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }

    public static String timeNow () {
        return "[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "]";
    }
}