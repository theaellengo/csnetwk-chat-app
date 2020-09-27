import java.io.*;
import java.net.*;

public class ClientConnection extends Thread {
    
    Socket endpoint;
    Client client;
    DataInputStream reader;
    DataOutputStream writer;
    String type = "msg";
    Boolean clientsmessage = false;
    Boolean run = true;
    
    public ClientConnection(Socket endpoint, Client client) {
        this.endpoint = endpoint;
        this.client = client;
    }

    public void sendToServer(String msg) {
        try {
            type = "msg";
            writer.writeUTF(type);
            clientsmessage = true; //this.client message to server
            writer.writeUTF(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFileToServer(int bytecount, DataInputStream dataInput) {
        try {
            type = "file";
            writer.writeUTF(type);
            clientsmessage = true;
            byte[] allocbytes = new byte[bytecount];
            writer.writeInt(bytecount);
            dataInput.read(allocbytes, 0, allocbytes.length);
            writer.write(allocbytes, 0, allocbytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFromServer(String sender, String msg) {
        String message = "";
        try {
            if (clientsmessage) {
                message = "You: " + msg + "\n\n";
                System.out.println(message);
            } else {
                message = sender + ": " + msg + "\n\n";
                System.out.println(message);
            }
//            System.out.println(msg);
            clientsmessage = false; //set false after message has been received
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public void readFileFromServer(String sender){
        try {
            int bytesize = reader.readInt();
            byte[] allocbytes = new byte[bytesize];
            reader.read(allocbytes, 0, allocbytes.length);
            try {
                File filename = new File("RCVD.MD"); //have to change
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
            clientsmessage = false;
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

    public void terminateConnection() {
        this.run = false;
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
                    type = reader.readUTF(); //this comes from client, which is closed
                    if (type.equals("file")) {
                        readFileFromServer(reader.readUTF());
                    } else {
//                        readFromServer(reader.readUTF(), reader.readUTF());
                        client.messageArea.append(readFromServer(reader.readUTF(), reader.readUTF()));
                    }
                } catch (Exception e) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}