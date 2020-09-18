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
            clientsmessage = true; //client message
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
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
        }
    }

    @Override
    public void run() {
        try {
            //first write is the name
            writer.writeUTF(client.name);

            //reads from server
            while (running) {
                readFromServer(reader.readUTF(), reader.readUTF());
            }

            writer.writeUTF("END"); //sends terminatiion condition to the server
            System.out.println("You have disconnected from the chat");

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
}