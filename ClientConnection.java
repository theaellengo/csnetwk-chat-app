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

    BufferedImage img = null;
    
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

    public void sendFile(String file) {
        try {
            type = "file";
            System.out.println("Retrieving image " + file + "...");
            img = ImageIO.read(new File(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(img, "jpg", baos);
            baos.flush();

            byte[] bytes = baos.toByteArray();
            baos.close();

            System.out.println("Sending image to chatmate...");

            OutputStream out = endpoint.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);

            System.out.println("Image sent to chatmate!");

            dos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveFile() {
        try {
            type = "file";
            InputStream in = endpoint.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            int len = dis.readInt();
            System.out.println("Image size: " + (len/1024) + "KB");
            sendFileToServer(len, dis);

            byte[] data = new byte[len];
            dis.readFully(data);

            dis.close();

            InputStream inputStream = new ByteArrayInputStream(data);
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            client.receiveFile(bufferedImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile(File file) throws IOException {
        try {
            System.out.println("Downloading file...");
            URL url = file.toURL();

            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(file.toString());
            byte[] buffer = new byte[2048];

            int length = 0;

            while((length = inputStream.read(buffer)) != -1) {
                System.out.println("Buffer Read of length: " + length);
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();

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
//            clientsmessage = false; //set false after message has been received
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
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

    public void readFileFromServer(String sender){
        try {
            type = "file";
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
                System.out.print("You ");
            } else {
                System.out.print(sender + " ");
            }
            System.out.println("sent a file.");
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