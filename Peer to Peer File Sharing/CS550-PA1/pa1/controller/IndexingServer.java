package controller;

import java.net.*;
import common.BaseBacking;
import designPatterns.Singleton;
import providers.ServerThread;


public class IndexingServer {
	
	/*
	 * ServerSoket to get or receive contact messages, data or anything from clients or peers
	 * Socket is to send data, message, or contact to clients or peers.
	 */
	private ServerSocket serverSocket;
	private Socket socket;
	private BaseBacking baseBacking;
	/*
	 * When IndexingServer invoked the constructor would start first.
	 * So, we instantiate the serverSocket and socket with null values.
	 */
	public IndexingServer() throws Exception {
		serverSocket = null;
		socket = null;
		baseBacking = new BaseBacking();
		startServer();
	}

	public void startServer() throws Exception {
		Singleton.getInstance().setPeerCount(0);
		try {
			// Opening server socket connection with port number 5000
			serverSocket = new ServerSocket(5000);
			// Call ResourceBundle from BaseBacking to get the message.
			baseBacking.getMessage("SERVER_CONNECTION",
					InetAddress.getLocalHost().getHostAddress() + "\nWaiting for peer contact...");
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				// Accepting a peer requests to send data
				socket = serverSocket.accept();
				Singleton.getInstance().setPeerCount(Singleton.getInstance().getPeerCount() + 1);
				baseBacking.getMessage("PEER_CONNECTION", Singleton.getInstance().getPeerCount());
				// To start a new server's thread passing the socket and the current file array
				new Thread(new ServerThread(socket, Singleton.getInstance().getArrayFiles())).start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws Exception {
		// Invoking server class object
		new IndexingServer();
	}
}
