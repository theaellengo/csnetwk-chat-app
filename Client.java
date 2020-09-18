import java.net.*;
import java.util.*;

public class Client {

    ClientConnection connection;
    Scanner sc = new Scanner(System.in);
    String name;

    public static void main(String[] args){
        new Client();
    }

    //client constructor
    public Client(){

        String msg;
        String host;
        int port;

        try {
            //Ask for client details
            System.out.print("Name: "); name = sc.nextLine();
            System.out.print("host: "); host = sc.nextLine();
            System.out.print("port: "); port = sc.nextInt(); sc.nextLine(); //buffer

            Socket endpoint = new Socket(host, port);
            connection = new ClientConnection(endpoint, this);
            connection.start();
            
            //temporary termination condition; exits loop if msg is "END"
            while (true) {
                msg = sc.nextLine();
                if (!(msg.equals("END"))) {
                    sendMsg(msg);
                } else break;
            }

            connection.close();
            
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