import java.net.*;
import java.util.*;
import java.io.*;

public class Client {

    ClientConnection connection;
    Scanner sc = new Scanner(System.in);
    String name;

    public static void main(String[] args){
        new Client();
    }

    public Client(){

        String host;
        int port;

        try {
            
            System.out.print("Name: "); name = sc.nextLine();
            System.out.print("host: "); host = sc.nextLine();
            System.out.print("port: "); port = sc.nextInt(); 
            sc.nextLine(); //buffer

            Socket endpoint = new Socket(host, port);
            connection = new ClientConnection(endpoint, this);
            connection.start();
            
            listenForMessages();

            sc.close();
            connection.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenForMessages () {
        String msg;
        while (true) {
            msg = sc.nextLine();
            if (!(msg.equals("END"))) {
                if (msg.equals("FILE")){
                    getFile();
                } else {
                    sendMsg(msg);
                }
            } else break;
        }
    }

    //passes message to clientconnection
    public void sendMsg(String msg) {
        try {
            connection.sendToServer(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get file upload from client
    public void getFile() {
        try {

            System.out.println("Enter filename: ");
            String filename = sc.nextLine();
            File file = new File(filename);
            FileInputStream fileInput = new FileInputStream(file);
            DataInputStream dataInput = new DataInputStream(fileInput);

            int bytecount = fileInput.available();
            connection.sendFileToServer(bytecount, dataInput);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DID NOT SEND FILE TO CLIENT CONNECTION");
        }
        
    }

}