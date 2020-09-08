import java.net.*;
import java.io.*;

// CSNETWK S13 - Group 3
// Go, Thea Ellen
// Mangune, Alexandra Cyrielle

public class ChatServer
{
	public static void main(String[] args)
	{
		int nPort = Integer.parseInt(args[0]);
		System.out.println("Server: Listening on port " + args[0] + "...");
		ServerSocket serverSocket;
		Socket serverEndpointA;
		Socket serverEndpointB;

		try 
		{
			serverSocket = new ServerSocket(nPort);

			serverEndpointA = serverSocket.accept();
			System.out.println("Server: New client connected: " + serverEndpointA.getRemoteSocketAddress());
			DataInputStream disReader = new DataInputStream(serverEndpointA.getInputStream());
			String aname = disReader.readUTF();
			String amsg = disReader.readUTF();

			serverEndpointB = serverSocket.accept();
			System.out.println("Server: New client connected: " + serverEndpointB.getRemoteSocketAddress());
			disReader = new DataInputStream(serverEndpointB.getInputStream());
			String bname = disReader.readUTF();
			String bmsg = disReader.readUTF();

			DataOutputStream dosWriter = new DataOutputStream(serverEndpointA.getOutputStream());
			dosWriter.writeUTF("Message from " + bname + ": " + bmsg);

			dosWriter = new DataOutputStream(serverEndpointB.getOutputStream());
			dosWriter.writeUTF("Message from " + aname + ": " + amsg);

			serverEndpointA.close();
			serverEndpointB.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Server: Connection terminated.");
		}
	}
}