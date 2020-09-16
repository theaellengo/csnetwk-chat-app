import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    ServerSocket ss;
    Socket endpoint;
    DataInputStream reader;
    DataOutputStream writer;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Server ();
    }

    public Server() {

        int port;

        try {
            //ask server for portnum
            System.out.print("Port: ");
            port = sc.nextInt(); sc.nextLine(); //scanner for portnum + buffer
            System.out.println("Server is now listening at port " + port);

            // new server socket
            ss = new ServerSocket(port);
            endpoint = ss.accept();
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());
            String name = reader.readUTF();

            System.out.println("Server: Client connected at " + endpoint.getRemoteSocketAddress());

            while (true) {
                writer.writeUTF(name + ": " + reader.readUTF()); //echo
            }

            //closes connections
            //reader.close();
            //writer.close();
            //endpoint.close();
            //ss.close(); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}