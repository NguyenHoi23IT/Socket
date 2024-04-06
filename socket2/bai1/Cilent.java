import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
