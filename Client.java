import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String msg;
        String name;
        String host;
        int port;
        try {

            System.out.print("Name: ");
            name = sc.nextLine();
            System.out.print("host: ");
            host = sc.nextLine();
            System.out.print("port: ");
            port = sc.nextInt();

            sc.nextLine();

            Socket endpoint = new Socket(host, port);

            System.out.println("Client: Has connected to server " + host + ":" + port);

            DataInputStream reader = new DataInputStream(endpoint.getInputStream());
            DataOutputStream writer = new DataOutputStream(endpoint.getOutputStream());

            writer.writeUTF(name);

            System.out.print("> ");
            while (!(msg = sc.nextLine()).equals("END")) {
                writer.writeUTF(msg);
                System.out.println(reader.readUTF());
                System.out.print("> ");
            }

            writer.writeUTF("END");

            System.out.println("Client: has terminated connection");

            endpoint.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}