import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
>>>>>>> 933745baa5f5d853fccbeff7122d0e914b03c4ae
import java.net.Socket;
import java.util.Scanner;

public class Client {

    /* BACK-END components **/
    ClientConnection connection;
    Scanner sc = new Scanner(System.in);
    String name;
    String host;
    int port;

<<<<<<< HEAD
    // main chat area
    JFrame frame = new JFrame("De La Salle Usap");
    JPanel panel = new JPanel();
    JTextField textField = new JTextField(35);
    JTextArea messageArea = new JTextArea(40, 50);
    JButton sendText = new JButton("Send");
    JButton sendFile = new JButton("+");
    JButton logout = new JButton("Logout");

    public Client() {
        this.name = getName();
        this.host = getHost();
        this.port = getPort();

        // testing
        System.out.println("Name: " + name);
        System.out.println("host: " + host);
        System.out.println("port: " + port);

        // connect to the server
        connect(host, port);

        textField.setEditable(true);
        messageArea.setEditable(false);
        panel.setPreferredSize(new Dimension(50, 50));
        panel.setLayout(new FlowLayout());
        panel.add(textField); panel.add(sendFile); panel.add(sendText);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(logout, BorderLayout.NORTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        sendText.addActionListener(event -> {
            listenForMessages(textField.getText());
            textField.setText("");
        });

        sendFile.addActionListener(event -> {
            getFile();
        });

        logout.addActionListener(event -> {
            // TODO: ask for logs then disconnect
            disconnect();
            System.exit(0);
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Username: ", "",
                JOptionPane.PLAIN_MESSAGE);
    }

    private String getHost() {
        return JOptionPane.showInputDialog(frame, "Host Address: ", "",
                JOptionPane.PLAIN_MESSAGE);
    }

    private int getPort() {
        return Integer.parseInt(JOptionPane.showInputDialog(frame, "Port Number: ", "",
                JOptionPane.PLAIN_MESSAGE));
    }

    private void connect(String host, int port) {
        try {
            Socket endpoint = new Socket(host, port);
            connection = new ClientConnection(endpoint, this);
            connection.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            connection.terminateConnection();
            sc.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
=======
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
>>>>>>> 933745baa5f5d853fccbeff7122d0e914b03c4ae
    }

    public void listenForMessages(String message) {
        this.sendMsg(message);
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

    // main method
    public static void main(String[] args) {
        Client client = new Client();
        client.frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Client();

    }

}