import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    ServerSocket ss;
    Socket endpoint;
    DataInputStream reader;
    Boolean run;
    Scanner sc = new Scanner(System.in);
    String logs = "";
    ArrayList<Connection> connections = new ArrayList<Connection>();

    public static void main(String[] args) {
        new Server ();
    }

    public Server() {

        int port;
        run = true;

        try {
            port = 5000;
            ss = new ServerSocket(port);
            System.out.println("Server running at port " + port);
            //server listens until no connections left
            while (run) {
                acceptconnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptconnection() {
        try {
            endpoint = ss.accept();
            reader = new DataInputStream(endpoint.getInputStream());
            String name = reader.readUTF(); //read from clientconnection
            Connection c = new Connection(endpoint, this, name);
            c.start();
            connections.add(c); //adds connection to list of active connections
            System.out.println("Server: Client connected at " + endpoint.getRemoteSocketAddress());
            addLogs("Server: Client connected at " + endpoint.getRemoteSocketAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLogs(String log){
        logs = logs + log + "\n";
    }

    public String getLogs() {
        return logs;
    }

    //temporary. should askclient to download, not server.
    //will implement when there's file sending.
    public void askToDownload(){
        System.out.println("Download logs? (Y/N)");
        if (sc.nextLine().equals("Y")) {
            try (PrintWriter out = new PrintWriter("logs.txt")) {
                out.println(logs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeserver() {
        this.run = false;
        try {
            sc.close();
            reader.close();
            endpoint.close();
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}