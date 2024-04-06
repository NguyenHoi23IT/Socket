import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(serverReader.readLine()); // Read prompt to enter username
            String username = consoleReader.readLine();
            serverWriter.println(username);

            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = serverReader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String input;
            while ((input = consoleReader.readLine()) != null) {
                serverWriter.println(input);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
