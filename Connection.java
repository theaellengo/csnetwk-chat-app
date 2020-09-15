import java.io.*;
import java.net.*;

public class Connection extends Thread {

    private Socket s;
    private String name;

    public Connection(Socket s, String name) {
        this.s = s;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            String msg;
            DataInputStream reader = new DataInputStream(s.getInputStream());
            DataOutputStream writer = new DataOutputStream(s.getOutputStream());
            
            while (!(msg = reader.readUTF()).equals("END")) {
                writer.writeUTF(name + ": " + msg);
            }

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server: Client " + name + " at " + s.getRemoteSocketAddress() + " has disconnected");
        }
    }

}