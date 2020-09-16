import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    ClientConnection connection;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        new Client();
    }

    public Client(){

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
            connection = new ClientConnection(endpoint, this, name); //connection with client thread
            connection.start(); //start client thead connection
            
            while (true) {
                try {
                    connection.sendMsg(sc.nextLine());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}