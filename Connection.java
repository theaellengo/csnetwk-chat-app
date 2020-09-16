import java.io.*;
import java.net.*;
import java.util.*;

public class Connection extends Thread {

    Socket endpoint;
    Server server;
    String name;
    DataInputStream reader;
    DataOutputStream writer;

    public Connection(Socket endpoint, Server server, String name) {
        this.endpoint = endpoint;
        this.server = server;
        this.name = name; //connection associated with identifier
    }

    //sends message to all clients
    public void sendToAll(String name, String msg) {
        for (int i = 0; i < server.connections.size(); i++) {
            Connection c = server.connections.get(i);
            sendToClient(name, msg);
        }
    }

    public void sendToClient(String name, String msg) {
        try {
            writer.writeUTF(name);
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());
            
            
            while (true) {
                try {
                    sendToAll(name, reader.readUTF());
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            //closes connections
            reader.close();
            writer.close();
            endpoint.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}