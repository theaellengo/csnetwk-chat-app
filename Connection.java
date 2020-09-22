import java.io.*;
import java.net.*;
import java.time.*;

public class Connection extends Thread {

    Socket endpoint;
    Server server;
    String name;
    DataInputStream reader;
    DataOutputStream writer;
    Boolean run = true;

    public Connection(Socket endpoint, Server server, String name) {
        this.endpoint = endpoint;
        this.server = server;
        this.name = name; //client associated with connection
    }

    //sends string to server to client i
    public void sendMsgToClient(String sender, String msg) {
        try {
            writer.writeUTF(sender);
            writer.writeUTF(msg);
        } catch (Exception e) {
            System.out.println("[" + LocalTime.now() + "] Server: message sending failed");
            server.addLogs("[" + LocalTime.now() + "] Server: message sending failed");
        }
    }

    public void sendFileToClient(String sender, int allocsize, byte[] bytes) {
        try {
            writer.writeUTF(sender);
            writer.writeInt(allocsize);
            writer.write(bytes);
        } catch (Exception e) {
            System.out.println("Send File to clients nono");
            e.printStackTrace();
        }
    }

    public void sendToAll(String sender, String msg) {
        for (int i = 0; i < server.connections.size(); i++) {
            Connection c = server.connections.get(i);
            if (run && !endpoint.getRemoteSocketAddress().equals(server.connections.get(i).endpoint.getRemoteSocketAddress())) {
                System.out.println("[" + LocalTime.now() + "] Client " + endpoint.getRemoteSocketAddress() + 
                    " sent a message to " + server.connections.get(i).endpoint.getRemoteSocketAddress());
                server.addLogs("[" + LocalTime.now() + "] Client " + endpoint.getRemoteSocketAddress() + 
                    " sent a message to " + server.connections.get(i).endpoint.getRemoteSocketAddress());
            }
            c.sendMsgToClient(sender, msg);
        }
    }

    public void sendFileToAll(String sender, int allocsize, byte[] bytes) {
        for (int i = 0; i < server.connections.size(); i++) {
            Connection c = server.connections.get(i);
            if (run && !endpoint.getRemoteSocketAddress().equals(server.connections.get(i).endpoint.getRemoteSocketAddress())) {
                System.out.println("[" + LocalTime.now() + "] Client " + endpoint.getRemoteSocketAddress() + 
                    " sent a file to " + server.connections.get(i).endpoint.getRemoteSocketAddress());
                server.addLogs("[" + LocalTime.now() + "] Client " + endpoint.getRemoteSocketAddress() + 
                    " sent a file to " + server.connections.get(i).endpoint.getRemoteSocketAddress());
            }
            c.sendFileToClient(sender, allocsize, bytes);
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
            
            while (true) {
                try {
                    sendToAll(name, reader.readUTF());
                } catch (Exception e) {
                    break;
                } finally {
                    byte[] allocbytes = new byte[reader.readInt()];
                    reader.read(allocbytes, 0, allocbytes.length);
                    try {
                        File filename = new File("RCVD.MD");
                        FileOutputStream fileOutput = new FileOutputStream(filename);
                        fileOutput.write(allocbytes, 0, allocbytes.length);

                        fileOutput.close();
                    } catch (Exception e) {
                        System.out.println("DID NOT RECIEVE FILE AT CONNECTION");
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Server: Client at " + endpoint.getRemoteSocketAddress() + " has disconnected.");
            server.addLogs("Server: Client at " + endpoint.getRemoteSocketAddress() + " has disconnected.");
            server.connections.remove(this);
            run = false;

            sendToAll("Server", name + " has left the chat.");

            //closes server if no clients left
            if (server.connections.isEmpty()) {
                server.askToDownload();
                server.closeserver();
            }

            close();

        } catch (Exception e) {
            //e.printStackTrace(); //to not show in server
        }
    }
}