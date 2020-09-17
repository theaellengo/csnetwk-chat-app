import java.io.*;
import java.net.*;

public class ClientConnection extends Thread {
    
    String name;
    Boolean clientsmessage = false;
    Socket endpoint;
    Client client;
    DataInputStream reader;
    DataOutputStream writer;
    Boolean running = true; //to remove while-loop errors
    
    //constructor
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
        try {
            writer.writeUTF(msg);
            clientsmessage = true; //client message
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    public void readFromServer(String sender, String msg) {
        try {
            if (clientsmessage) //if the client connection is the one sending
                System.out.print("You: ");
            else System.out.print(sender + ": ");
            System.out.println(msg);
            clientsmessage = false;
        } catch (Exception e) {
            e.printStackTrace();
            close();
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
                String sender = reader.readUTF();
                String message = reader.readUTF();
                readFromServer(sender, message);
            }

            writer.writeUTF("END"); //sends terminatiion condition to the server
            System.out.println("You have disconnected from the chat");
            close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}