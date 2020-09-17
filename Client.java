import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    ClientConnection connection;
    DataInputStream reader;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        new Client();
    }

    public Client(){

        String msg;
        String name;
        String host;
        int port;

        //Ask for client details
        try {
            System.out.print("Name: "); name = sc.nextLine();
            System.out.print("host: "); host = sc.nextLine();
            System.out.print("port: "); port = sc.nextInt();
            sc.nextLine(); //buffer

            Socket endpoint = new Socket(host, port);
            connection = new ClientConnection(endpoint, this, name);
            connection.start();
            
            while (true) {
                msg = sc.nextLine();
                if (!(msg.equals("END"))) {
                    sendMsg(msg);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            connection.sendToServer(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}