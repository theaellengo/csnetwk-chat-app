import java.net.*;
import java.util.*;

public class Client {

    ClientConnection connection;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        new Client();
    }

    //client constructor
    public Client(){

        String msg;
        String name;
        String host;
        int port;

        try {
            //Ask for client details
            System.out.print("Name: "); name = sc.nextLine();
            System.out.print("host: "); host = sc.nextLine();
            System.out.print("port: "); port = sc.nextInt();
            sc.nextLine(); //buffer

            Socket endpoint = new Socket(host, port);
            connection = new ClientConnection(endpoint, this, name);
            connection.start();
            
            //temporary termination condition
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

    //passes message to clientconnection
    public void sendMsg(String msg) {
        try {
            connection.sendToServer(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}