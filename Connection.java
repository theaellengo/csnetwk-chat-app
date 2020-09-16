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

    public void sendStringToClient(String msg) {
        try {
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String msg) {
        for (int i = 0; i < server.connections.size(); i++) {
            Connection c = server.connections.get(i);
            c.sendStringToClient(msg);
        }
    }

    @Override
    public void run() {
        try {
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());
            
            
            while (true) {
                sendToAll(name + ": " + reader.readUTF());
            }

            //closes connections
            //reader.close();
            //writer.close();
            //endpoint.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}