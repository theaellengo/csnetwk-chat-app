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
    public void sendStringToClient(String msg) {
        try {
            writer.writeUTF(name);
            writer.flush();
            writer.writeUTF(msg);
            writer.flush();
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
            
            while (running) {
                try {
                    sendToAll(reader.readUTF());
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