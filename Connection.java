import java.io.*;
import java.net.*;

public class Connection extends Thread {

    Socket endpoint;
    Server server;
    String name;
    Boolean running = true;
    DataInputStream reader;
    DataOutputStream writer;

    public Connection(Socket endpoint, Server server, String name) {
        this.endpoint = endpoint;
        this.server = server;
        this.name = name; //client associated with connection
    }

    //sends string to server to connecting client
    public void sendMsgToClient(String sender, String msg) {
        try {
            writer.writeUTF(sender);
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String sender, String msg) {
        for (int i = 0; i < server.connections.size(); i++) {
            Connection c = server.connections.get(i);
            c.sendMsgToClient(sender, msg);
        }
    }

    @Override
    public void run() {
        try {
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());
            
            while (running) {
                try {
                    sendToAll(name, reader.readUTF());
                } catch (Exception e) {
                    break; //break when client ends connection
                }
            }

            System.out.println("Server: Client at " + endpoint.getRemoteSocketAddress() + " has disconnected.");
            server.connections.remove(this);
            sendToAll(name, "END");

            //closes connections
            reader.close();
            writer.close();
            endpoint.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}