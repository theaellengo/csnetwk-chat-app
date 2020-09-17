import java.io.*;
import java.net.*;

public class ClientConnection extends Thread {
    
    String name;
    Boolean running = true;
    Boolean clientsmessage;
    Socket endpoint;
    Client client;
    DataInputStream reader;
    DataOutputStream writer;
    
    public ClientConnection(Socket endpoint, Client client, String name) {
        this.endpoint = endpoint;
        this.client = client;
        this.name = name;
        try {
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sends to server
    public void sendToServer(String msg) {
        //will write to server until client types in END
            try {
                writer.writeUTF(msg);
                clientsmessage = true; //your message
                //System.out.print("> "); 
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void readFromServer(String msg) {
        try {
            if (clientsmessage) System.out.print("> ");
            System.out.println(msg);
            clientsmessage = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //closes connections
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
            //first write is the name
            writer.writeUTF(name);
            writer.flush();

            //reads from server
            while (running) {
                readFromServer(reader.readUTF());
            }

            writer.writeUTF("END"); //sends terminatiion condition to the server
            System.out.println("You have disconnected from the chat");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}