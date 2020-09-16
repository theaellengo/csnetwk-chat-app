import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    Socket endpoint;
    DataInputStream reader;
    DataOutputStream writer;
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

            endpoint = new Socket(host, port);
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());

            //first write is the name
            writer.writeUTF(name);
            
            System.out.print("> ");
            //will read and write to server until client types in END
            while (!(msg = sc.nextLine()).equals("END")) {
                writer.writeUTF(msg);
                System.out.println(reader.readUTF());
                System.out.print("> "); 
            }

            //connecting to the Server
            writer.writeUTF("END"); //sends END to the server
            System.out.println("You have disconnected from the chat");

            //close connections
            reader.close();
            writer.close();
            endpoint.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}