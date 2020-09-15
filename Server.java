import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        int port = 5000; // Can be changed

        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Sever: Listening to port " + port);

            while (true) {
                // Waits for a client to connect
                Socket endpoint = ss.accept();
                DataInputStream reader = new DataInputStream(endpoint.getInputStream());
                DataOutputStream writer = new DataOutputStream(endpoint.getOutputStream());
                String name = reader.readUTF();

                System.out.println("Server: Client " + name + " at " + endpoint.getRemoteSocketAddress() + " has connected");

                // Make the Thread Object
                Connection connect = new Connection(endpoint, name);
                // Start the thread
                connect.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}