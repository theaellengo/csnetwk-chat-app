import java.io.*;
import java.net.*;

public class ClientConnection extends Thread {
    
    Socket endpoint;
    Client client;
    DataInputStream reader;
    DataOutputStream writer;
    Boolean clientsmessage = false;
    Boolean running = true;
    
    public ClientConnection(Socket endpoint, Client client) {
        this.endpoint = endpoint;
        this.client = client;
    }

    public void sendToServer(String msg) {
        try {
            clientsmessage = true; //this.client message to server
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromServer(String sender, String msg) {
        try {                
            if (clientsmessage) {
                System.out.print("You: ");
            } else {
                System.out.print(sender + ": ");
            }
            System.out.println(msg);
            clientsmessage = false; //set false after message has been received
        } catch (Exception e) {
            e.printStackTrace();
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
            writer.writeUTF(client.name); //passes name to server

            //keeps listening for <sender, message> until connection terminated
            while (running) { readFromServer(reader.readUTF(), reader.readUTF()); }

            writer.writeUTF("END"); //sends termination condition to server
            System.out.println("You have disconnected from the chat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}