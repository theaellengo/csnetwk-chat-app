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

        String name;
        String host;
        int port;

        //Ask for client details
        try {
            System.out.print("Name: "); name = sc.nextLine();
            System.out.print("host: "); host = sc.nextLine();
            System.out.print("port: "); port = sc.nextInt();
            sc.nextLine(); //buffer

<<<<<<< HEAD
            Socket endpoint = new Socket(host, port);
            connection = new ClientConnection(endpoint, this, name); //connection with client thread
            connection.start(); //start client thead connection
            
            while (true) {
                try {
                    connection.sendMsg(sc.nextLine());
                } catch (Exception e) {
                    e.printStackTrace();
=======
            endpoint = new Socket(host, port);
            reader = new DataInputStream(endpoint.getInputStream());
            writer = new DataOutputStream(endpoint.getOutputStream());

            //first write is the name
            writer.writeUTF(name);
            
            System.out.print("> ");
            try {
                while (true) 
                {
                    //will read and write to server until client types in END
                    if (!(msg = sc.nextLine()).equals("END")) {
                    //catch error reading writing to server
                        try {
                            writer.writeUTF(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                
                    System.out.println(reader.readUTF());
                    System.out.print("> "); 

>>>>>>> parent of d94def3... connection :)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            

            

            //connecting to the Server
            writer.writeUTF("END"); //sends END to the server
            System.out.println("You have disconnected from the chat");
            
            //closes connections
            reader.close();
            writer.close();
            endpoint.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}