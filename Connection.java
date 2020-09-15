import java.io.*;
import java.net.*;

public class Connection extends Thread {

    private Socket s;
    private String name;
    private DataInputStream b;

    public Connection(Socket s, String name, DataInputStream b) {
        this.s = s;
        this.name = name;
        this.b = b;
    }

    @Override
    public void run() {
        try {
            String msg;
            DataInputStream reader = new DataInputStream(s.getInputStream());
            DataOutputStream writer = new DataOutputStream(s.getOutputStream());
            
            // This checks whether the string that was sent from
            // the client side is the terminal "END" else we
            // send the string back to the client
            while (!(msg = reader.readUTF()).equals("END")) {
                writer.writeUTF(name + ": " + msg);
                b.readUTF(name + ": " + msg);
            }

            s.close();
        } catch (Exception e) {
            // e.printStackTrace(); // Uncomment this if you want to look at the error thrown
        } finally {
            System.out.println("Server: Client " + s.getRemoteSocketAddress() + " has disconnected");
        }
    }

}