import java.io.*;
import java.net.*;

public class ClientConnection extends Thread {
    
    Socket endpoint;
    Client client;
    DataInputStream reader;
    DataOutputStream writer;
    Boolean clientsmessage = false;
    Boolean run = true;
    
    public ClientConnection(Socket endpoint, Client client) {
        this.endpoint = endpoint;
        this.client = client;
    }

    public void sendToServer(String msg) {
        try {
            writer.writeUTF("msg");
            clientsmessage = true; //this.client message to server
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFileToServer(int bytecount, DataInputStream dataInput) {
        try {
            writer.writeUTF("file");
            clientsmessage = false;
            byte[] allocbytes = new byte[bytecount];
            writer.writeInt(bytecount);
            dataInput.read(allocbytes, 0, allocbytes.length);
            writer.write(allocbytes, 0, allocbytes.length);
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

    public void readFileFromServer(){
        try {
            String sender = reader.readUTF();
            int bytesize = reader.readInt();
            byte[] allocbytes = new byte[bytesize];
            reader.read(allocbytes, 0, allocbytes.length);
            try {
                File filename = new File("RCVD.MD");
                FileOutputStream fileOutput = new FileOutputStream(filename);
                fileOutput.write(allocbytes, 0, allocbytes.length);
                fileOutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (clientsmessage) {
                System.out.print("You: ");
            } else {
                System.out.print(sender + ": ");
            }
            System.out.println("sent a file");
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
            while (run) { 
                try {
                    String type = reader.readUTF();
                    if (type.equals("file")) {
                        readFileFromServer();
                    } else {
                        readFromServer(reader.readUTF(), reader.readUTF()); 
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            writer.writeUTF("END"); //sends termination condition to server

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}