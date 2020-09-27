import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    ClientConnection connection;
    Scanner sc = new Scanner(System.in);
    String name;
    String host;
    int port;

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
            try {
                JFileChooser chooseFile = new JFileChooser();
                chooseFile.showSaveDialog(null);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, e, "Error", JOptionPane.ERROR_MESSAGE);
            }

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

}