package controller;

import java.net.*;
import providers.PeerServerThread;

public class PeerServer implements Runnable {

	/*
	 * We can summarize this class as peer acting as server for different threads to retrieve a directory.
	 * Or we can say that we are starting a thread for peer to act as a server.
	 * peerServerSocket to get or receive contact messages, data or anything from other clients or peers
	 * Socket is to send data, message, or contact to other clients or peers.
	 * 
	 */
	private ServerSocket peerServerSocket;
	private Socket peerSocket;
	private int peerServerPort;
	private String peerServerDir;

	/*
	 * A constructor that take two parameters 
	 * to start thread for peer to act as a server.
	 * This class will pass two parameters to PeerServerThread
	 */
	public PeerServer(int peerServerPort, String peerServerDir) {
		this.peerServerPort = peerServerPort;
		this.peerServerDir = peerServerDir;
	}

	public void run() {
		try {
			/*
			 * Make PeerServer connection using the passed two
			 * parameters peerServerPort.
			 * after accepting it, pass them to the PeerServerThread Class to create a new thread.
			 */
			peerServerSocket = new ServerSocket(peerServerPort);
			peerSocket = peerServerSocket.accept();
			new Thread(new PeerServerThread(peerSocket, peerServerDir)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
