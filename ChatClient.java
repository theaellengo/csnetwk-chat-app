import java.net.*;
import java.io.*;

// CSNETWK S13 - Group 3
// Go, Thea Ellen
// Mangune, Alexandra Cyrielle

public class ChatClient
{
	public static void main(String[] args)
	{
		String sServerAddress = args[0];
		int nPort = Integer.parseInt(args[1]);
		String sUser = args[2];
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		
		try
		{
			Socket clientEndpoint = new Socket(sServerAddress, nPort);
			
			System.out.println(sUser + ": Connecting to server at " + clientEndpoint.getRemoteSocketAddress());
			System.out.println(sUser + ": Connected to server at " + clientEndpoint.getRemoteSocketAddress());
			
			//start loop? thread? here i guess
			String sMessage = reader.readLine();
			System.out.println("You: " + sMessage);
			
			DataOutputStream dosWriter = new DataOutputStream(clientEndpoint.getOutputStream());
			dosWriter.writeUTF(sUser);
			dosWriter.writeUTF(sMessage);
			
			DataInputStream disReader = new DataInputStream(clientEndpoint.getInputStream());
			System.out.println(disReader.readUTF());
			//end here
			
			clientEndpoint.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println(sUser + ": Connection terminated.");
		}
	}
}