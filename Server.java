import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    ServerSocket ss;
    Socket endpoint;
    DataInputStream reader;
    DataOutputStream writer;
    Scanner sc = new Scanner(System.in);
    ArrayList<Connection> connections = new ArrayList<Connection>();

    public static void main(String[] args) {
        new Server ();
    }

    public Server() {

        int port;

        try {
            //ask server for portnum
            System.out.print("Port: ");
            port = sc.nextInt(); sc.nextLine(); //scanner for portnum + buffer
            ss = new ServerSocket(port);
            System.out.println("Server running at port " + port);

            //server will continue to run until no connections left
            while(true) {
                endpoint = ss.accept();
                reader = new DataInputStream(endpoint.getInputStream());
                writer = new DataOutputStream(endpoint.getOutputStream());
                String name = reader.readUTF();

                Connection c = new Connection(endpoint, this, name);
                c.start();
                connections.add(c);
                
                System.out.println("Server: Client connected at " + endpoint.getRemoteSocketAddress());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}