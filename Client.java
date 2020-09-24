import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    /* BACK-END components **/
    ClientConnection connection;
    Scanner sc = new Scanner(System.in);
    String name;
    String host;
    int port;

    JTextField nameField;
    JTextField hostField;
    JTextField portField;

    /* CLIENT constructor **/
    public Client() {

        /* FRONT-END
           First panel - asks the user for the necessary
           details before proceeding to the main panel
          **/

        JFrame mainFrame = new JFrame();

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        mainPanel.setLayout(new GridLayout(5,1));

        JLabel test1 = new JLabel("Testing main panel");
        mainPanel.add(test1);

        JFrame loginFrame = new JFrame();

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        panel.setLayout(new GridLayout(5,1));

        nameField = new JTextField("Enter name");
        nameField.setBounds(50,100, 300,50);

        hostField = new JTextField("Enter IP (Host) address");
        hostField.setBounds(50,100, 300,50);

        portField = new JTextField("Enter port number");
        portField.setBounds(50,100, 300,50);

        JButton loginButton = new JButton("Join Chat");
        loginButton.setBounds(50, 100, 300, 50);
        loginButton.addActionListener(event -> {
            /*
              Inputted texts from fields are transferred
              the the their respective variables
              **/
            name = nameField.getText();
            host = hostField.getText();
            port = Integer.parseInt(portField.getText());

            /* BACK-END
               connects a user to the server after providing
               the necessary information
              **/
            try {
                System.out.println("Name: " + name);
                System.out.println("host: " + host);
                System.out.println("port: " + port);

                loginFrame.setVisible(false);
                mainFrame.setVisible(true);

                Socket endpoint = new Socket(host, port);
                connection = new ClientConnection(endpoint, this);
                connection.start();

                listenForMessages();

                connection.terminateConnection();
                sc.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        panel.add(nameField);
        panel.add(hostField);
        panel.add(portField);
        panel.add(loginButton);

        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(200,500);
        loginFrame.setResizable(false);
        loginFrame.setTitle("De La Salle Usap");
        loginFrame.pack();
        loginFrame.setVisible(true);

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(200,500);
        mainFrame.setResizable(false);
        mainFrame.setTitle("De La Salle Usap");
        mainFrame.pack();
        mainFrame.setVisible(false);
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
        }
        
    }

    public static void main(String[] args) {
        new Client();

    }

}