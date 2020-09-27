import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

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

    public String readFromServer(String sender, String msg) {
        String message = "";
        try {
            if(clientsmessage) {
                message = "You: " + msg + "\n\n";
            }
            else {
                message = sender + ": " + msg + "\n\n";
            }
            clientsmessage = false; //set false after message has been received
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public void sendFileToServer(File filename) {
        try {
            type = "file";
            writer.writeUTF(type);
            clientsmessage = true;

            FileInputStream fileInput = new FileInputStream(filename);
            reader = new DataInputStream(fileInput);

            int byteCount = fileInput.available();
            byte[] buffer = new byte[byteCount];

            reader.read(buffer, 0, buffer.length);

            System.out.println("Sending file " + filename + " (" + filename.length() + " bytes)");

            writer = new DataOutputStream(endpoint.getOutputStream());
            writer.writeInt(buffer.length);
            writer.write(buffer, 0, buffer.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFileFromServer(String sender){
        String message="";
        try {
            type = "file";
            int bytesize = reader.readInt();
            byte[] allocbytes = new byte[bytesize];
            reader.read(allocbytes, 0, allocbytes.length);
            try {
                reader = new DataInputStream(endpoint.getInputStream());

                int fileLength = reader.readInt();
                byte[] buffer = new byte[fileLength];
                reader.read(buffer, 0, buffer.length);

                File filename = new File("test.jpg");
                FileOutputStream fileOutput = new FileOutputStream(filename);
                fileOutput.write(buffer, 0, buffer.length);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (clientsmessage) {
                System.out.print("You ");
                message = "You sent a file";
            } else {
                System.out.print(sender + " ");
                message = sender + "sent a file.";
            }
            System.out.println("sent a file.");
            clientsmessage = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
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
                        client.messageArea.append(readFileFromServer(reader.readUTF()));
                    } else {
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