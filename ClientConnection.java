import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnection extends Thread {
    
    String name;
    Socket endpoint;
    DataInputStream reader;
    DataOutputStream writer;
    
    public ClientConnection(Socket endpoint, Client client, String name) {
        this.endpoint = endpoint;
        this.name = name;

        try {
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sends to server
    public void sendMsg(String msg) {
        //will write to server until client types in END
        if (!(msg.equals("END"))) {
            try {
                writer.writeUTF(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            //first write is the name
            writer.writeUTF(name);

            //reads from server
            while (true) {
                try {
                    System.out.println(reader.readUTF());
                    System.out.print("> "); 
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                
            }

            writer.writeUTF("END"); //sends END to the server
            System.out.println("You have disconnected from the chat");
            
            //closes connections
            reader.close();
            writer.close();
            endpoint.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}