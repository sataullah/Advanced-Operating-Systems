package controller;

import java.io.*;
import java.net.Socket;

/*
 * PeerClient Class is to take a copy file from peerID following its directory path and store it 
 * at the new directory path of the current peerID.
 */
public class PeerClient{

	@SuppressWarnings("unused")
	private int peerServerID, peerServerPort;
	private String peerServerHost, peerDir, fileName;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	Socket peerClientSocket;
	
	/*
	 * The constructor takes necessary fields to retrieve a file
	 * from the server peer destination.
	 */
	public PeerClient(int peerServerID, int peerServerPort, String peerServerHost, String fileName, String peerDir) {
		this.peerServerID = peerServerID;
		this.peerServerPort = peerServerPort;
		this.peerServerHost = peerServerHost;
		this.fileName = fileName;
		this.peerDir = peerDir;
		peerAsClient();
	}

	@SuppressWarnings("resource")
	public void peerAsClient() {
		try {
			/*
			 * Here we using the host and the port of the peerID that we
			 * will take a copy file from its directory path, and make
			 * socket connection to it.
			 * No need for peerServerID since all peers are designed at the same machine.
			 * we just need these two information.
			 */
			peerClientSocket = new Socket(peerServerHost, peerServerPort);
			oos = new ObjectOutputStream(peerClientSocket.getOutputStream());
			ois = new ObjectInputStream(peerClientSocket.getInputStream());
			
			/*
			 * Writing file name to a server peer that requests a file.
			 */
			oos.writeObject(fileName);
			

			// Reading bytes of the file
			int readBytes = (int) ois.readObject();
			byte b[] = new byte[readBytes];
			ois.readFully(b);

			// Writing file to the client peer directory
			FileOutputStream os = new FileOutputStream(peerDir + "//" + fileName);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			bos.write(b, 0, (int) readBytes);
			System.out.println(
					"*The requested file: " + fileName + ", has been downloaded to the directory:\n" + peerDir);
			System.out.println(
					"*Display File Name: " + fileName);
			
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
