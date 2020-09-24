import java.io.*;
import java.net.*;
import java.time.*;

public class Connection extends Thread {

    Socket endpoint;
    Server server;
    String name;
    String type;
    DataInputStream reader;
    DataOutputStream writer;
    Boolean run;
    Boolean log;

    public Connection(Socket endpoint, Server server, String name) {
        this.endpoint = endpoint;
        this.server = server;
        this.name = name; //client associated with connection
        this.run = true;
        this.log = false;
    }

    //sends string to server to client i
    public void sendMsgToClient(String type, String sender, String msg) {
        try {
            writer.writeUTF(type);
            writer.writeUTF(sender);
            writer.writeUTF(msg);
        } catch (Exception e) {
            System.out.println("[" + LocalTime.now() + "] Server: message sending failed");
            server.addLogs("[" + LocalTime.now() + "] Server: message sending failed");
        }
    }

    public void sendToAll(String type, String sender, String msg) {
        for (int i = 0; i < server.connections.size(); i++) {
            Connection c = server.connections.get(i);
            c.sendMsgToClient(type, sender, msg);

            if (run && !c.equals(this) && log) {
                String log1 = "[" + LocalTime.now() + "] Client " + endpoint.getRemoteSocketAddress() + 
                " sent a message to " + server.connections.get(i).endpoint.getRemoteSocketAddress();
                System.out.println(log1);
                server.addLogs(log1);
                String log2 = "[" + LocalTime.now() + "] Client " + server.connections.get(i).endpoint.getRemoteSocketAddress() + 
                " received a message from " + endpoint.getRemoteSocketAddress();
                System.out.println(log2);
                server.addLogs(log2);
            }

        }
    }

    //can only support one other client, which is fine
    public void sendFileToOtherClient(String type, String name) {
        try {
            int bytesize = reader.readInt();
            byte[] allocbytes = new byte[bytesize];
            reader.read(allocbytes, 0, allocbytes.length);
            for (int i = 0; i < server.connections.size(); i++) {
                Connection c = server.connections.get(i);
                if (run && !c.equals(this)) {

                    c.writer.writeUTF(type);
                    c.writer.writeUTF(name);
                    c.writer.writeInt(bytesize);
                    c.writer.write(allocbytes, 0, allocbytes.length);

                    String log1 = "[" + LocalTime.now() + "] Client " + endpoint.getRemoteSocketAddress() + 
                    " sent a file to " + server.connections.get(i).endpoint.getRemoteSocketAddress();
                    System.out.println(log1);
                    server.addLogs(log1);
                    String log2 = "[" + LocalTime.now() + "] Client " + server.connections.get(i).endpoint.getRemoteSocketAddress() + 
                    " received a file from " + endpoint.getRemoteSocketAddress();
                    System.out.println(log2);
                    server.addLogs(log2);
                } else {
                    this.log = false;
                    sendMsgToClient("msg", name, " sent a file");
                    this.log = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + LocalTime.now() + "] Server: file sending failed");
            server.addLogs("[" + LocalTime.now() + "] Server: file sending failed");
        }
    }

    public void close() {
        try {
            reader.close();
            writer.close();
            endpoint.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());

            sendToAll("msg", "Server", this.name + " has entered the chat.");
            this.log = true;
            
            while (true) {
                try {
                    type = reader.readUTF();
                    if (type.equals("msg")) {
                        sendToAll(type, name, reader.readUTF());
                    } else {
                        sendFileToOtherClient(type, name);
                    }  
                } catch (Exception e) {
                    //e.printStackTrace();
                    break;
                } 
            }

            System.out.println("Server: Client at " + endpoint.getRemoteSocketAddress() + " has disconnected.");
            server.addLogs("Server: Client at " + endpoint.getRemoteSocketAddress() + " has disconnected.");
            server.connections.remove(this);
            run = false;

            sendToAll("msg", "Server", this.name + " has left the chat.");

            //closes server if no clients left
            if (server.connections.isEmpty()) {
                server.askToDownload();
                server.closeserver();
            }

            close();

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}