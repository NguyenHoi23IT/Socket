import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                Thread clientHandlerThread = new Thread(() -> {
                    try {
                        OutputStream outputStream = clientSocket.getOutputStream();
                        for (int i = 1; i <= 1000; i++) {
                            outputStream.write((i + "\n").getBytes());
                            outputStream.flush();
                            Thread.sleep(1000); // delay 1 second
                        }
                        clientSocket.close();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}