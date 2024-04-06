import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<String> usernames = new HashSet<>();
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                // Prompt the client to enter username
                writer.println("Enter your username:");
                username = reader.readLine();
                synchronized (usernames) {
                    if (!usernames.contains(username)) {
                        usernames.add(username);
                        for (PrintWriter pw : clientWriters) {
                            pw.println(username + " has joined the chat");
                        }
                        clientWriters.add(writer);
                    } else {
                        writer.println("Username already exists. Please choose another one.");
                        return;
                    }
                }

                String input;
                while ((input = reader.readLine()) != null) {
                    for (PrintWriter pw : clientWriters) {
                        pw.println(username + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(username + " has left the chat");
            } finally {
                if (username != null) {
                    usernames.remove(username);
                }
                if (writer != null) {
                    clientWriters.remove(writer);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
