package controller;

import java.io.*;
import java.net.*;
import java.util.*;

import common.BaseBacking;
import common.FileInfo;
import common.Request;
import common.Response;

/*
 * Peer Class represent the client side in which contact the indexing server
 * using request and respond objects so that get serializable accessibility to the 
 * server indexing files and get benefit from the services that server provides.
 * Here the Peer Class is acting as an interface for the most time of the connection
 * period that is take all peer information in the form of asking info or questions like
 * Enter PeerID ... and so on.
 * This Class has retrieve method that download a file from another peer.
 */
public class Peer {

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String peerDir, peerHost, serverHost, fileName;
	private int portNumber, peerID;
	private Request peerRequest;
	private Response serverResponse;
	private ArrayList<FileInfo> resultList, peerFiles;
	private BufferedReader br;
	private File folder, current;
	private File[] fileList;
	private FileInfo file;
	private BaseBacking baseBacking;

	/*
	 * When Peer invoked the constructor would start first.
	 * So, we instantiate the BufferedReader, peerFiles and 
	 * jump to the peerConnection method.
	 */
	public Peer() {
		br = new BufferedReader(new InputStreamReader(System.in));
		peerFiles = new ArrayList<>();
		baseBacking = new BaseBacking();
		peerConnection();
	}

	@SuppressWarnings("unchecked")
	public void peerConnection() {
		try {
			
			//collect necessary peer information to make connection
			peerInfo();
			//after collecting peer information, we connect to the indexing server
			peerSocket();
			//we make open while loop to make active connection
			while (true) {
				//here we store all the current peer files from given directory path
				getAllPeerFiles();
				// Call ResourceBundle from BaseBacking to get the message.
				baseBacking.getMessage("SERVICES");
				int choice = Integer.parseInt(br.readLine());
				peerRequest = new Request();
				serverResponse = new Response();
				
				/*
				 * Each peer can choose a service number.
				 * we have four services:
				 * 1- REGISTER
				 * 2- SEARCH
				 * 3- DEREGISTER
				 * 4- DISCONNECT
				 */
				switch (choice) {
				/*
				 * case 1: if there is no file at the given dir path, it will display No files found!
				 * otherwise, it will register all files of the current peer.
				 * be aware that it will register all files that are stored from the
				 * begging in peerFiles from getAllPeerFiles method.
				 */
				case 1:
					if (fileList.length == 0) {
						System.out.println("No files found!");
						continue;
					} else {
						serverReq("REGISTER", peerFiles);
						serverRes();
						if ((boolean) serverResponse.getResponseData()) {
							// Call ResourceBundle from BaseBacking to get the message.
							baseBacking.getMessage("REGISTER_SUC", peerID);
						}
					}
					break;

					/*
					 * case 2: it will ask for a file name and then it will
					 * pass the file name and the request type as "SEARCH"
					 * to lookup at the indexing server global files; therefore,
					 * it will get all peers that have the same file name
					 */
				case 2:
					// Searching for file at the indexing server
					// Call ResourceBundle from BaseBacking to get the message.
					baseBacking.getMessage("DOWNLOAD");
					fileName = br.readLine();
					serverReq("SEARCH", fileName);
					// Call ResourceBundle from BaseBacking to get the message.
					baseBacking.getMessage("WAIT");
					resultList = new ArrayList<FileInfo>();

					serverRes();
					resultList = (ArrayList<FileInfo>) serverResponse.getResponseData();
					
					/*
					 * When the respond comes back from the indexing server that 
					 * holds a result, it will check if a file is regestired or
					 * not; thus, if the file is not there it will print out
					 * not indexed at the server, otherwise, it will display all
					 * peers that have the same file name. Then, it will pass
					 * the file name to the retrieve method
					 */
					if (resultList.size() == 0) {
						System.out.println(fileName + " not indexed at the server");
					} else {
						for (int j = 0; j < resultList.size(); j++) {
							int pid = resultList.get(j).getPeerID();
							String host = resultList.get(j).getPeerHost();
							int port = resultList.get(j).getPortNumber();
							// Call ResourceBundle from BaseBacking to get the message.
							baseBacking.getMessage("ID", pid, "HOST", host, "PORT", port);
						}
						retrieve(fileName);
					}
					break;

				case 3:
					/*
					 * case 3: it will ask for a file name to delete its indexing registry
					 * from the indexing server. if the file not there, it will display
					 * is not indexed at the server, otherwise, it will remove it from the
					 * global array file at the indexing server by passing its name, peerID
					 * at the server side.
					 */
					// Call ResourceBundle from BaseBacking to get the message.
					baseBacking.getMessage("DELETE_FILE");
					fileName = br.readLine();
					serverReq("DEREGISTER", fileName);
					// Call ResourceBundle from BaseBacking to get the message.
					baseBacking.getMessage("WAIT");
					resultList = new ArrayList<FileInfo>();
					serverRes();
					resultList = (ArrayList<FileInfo>) serverResponse.getResponseData();
					if (resultList.size() == 0) {
						System.out.println(fileName + " is not indexed at the server");
					} else {
						// Call ResourceBundle from BaseBacking to get the message.
						baseBacking.getMessage("DEREGISTER_SUC");
					}

					break;
					//case 4: Close connection -- it is optional
				case 4:
					serverReq("DISCONNECT", "Disconnect request");
					// Call ResourceBundle from BaseBacking to get the message.
					baseBacking.getMessage("DISCO_SUC");
					System.exit(0);
					break;

				default:
					System.out.println("Not Valid Number.");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * this method is to get all a current
	 * peer files from a given dir path and
	 * add them to the peerFiles.
	 */
	private void getAllPeerFiles() {
		// Retrieving peer all files
		folder = new File(peerDir);
		fileList = folder.listFiles();
		peerFiles = new ArrayList<>();
		for (int i = 0; i < fileList.length; i++) {
			file = new FileInfo();
			current = fileList[i];
			file.setPeerID(peerID);
			file.setPeerHost(peerHost);
			file.setPortNumber(portNumber);
			file.setFileName(current.getName());
			peerFiles.add(file);
		}
	}

	/*
	 * This method is to retrieve file info
	 * of a current peer in the asking form.
	 */
	private void peerInfo() throws NumberFormatException, IOException {
		// Call ResourceBundle from BaseBacking to get the message.
		baseBacking.getMessage("PEER_ID");
		peerID = Integer.parseInt(br.readLine());
		baseBacking.getMessage("PEER_PORT");
		portNumber = Integer.parseInt(br.readLine());
		baseBacking.getMessage("PEER_DIR");
		peerDir = br.readLine();
		peerHost = InetAddress.getLocalHost().getHostAddress();
		baseBacking.getMessage("SER_HOST");
		serverHost = br.readLine();

	}

	/*
	 * After collecting peer information that 
	 * we need to contact the indexing server.
	 * we can establish it using portNumber for peerDir
	 * for PeerServer, and serverHost and the 5000 port number
	 * to establish the socket.
	 */
	public void peerSocket() throws UnknownHostException, IOException {
		// Starting thread for peer to act as a server
		PeerServer sd = new PeerServer(portNumber, peerDir);
		new Thread(sd).start();
		// Connecting with the indexing server
		socket = new Socket(serverHost, 5000);
		System.out.println("Connected to the server");
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		/*
		 * Important Note:
		 * When you run the test file, 
		 * commit the below line to 
		 * avoid java.lang.ClassCastException 
		 */
		oos.writeObject(peerID); //<--this line
	}

	
	public void serverReq(String Choice, Object requestData) throws IOException {
		// Request to server
		peerRequest = new Request();
		peerRequest.setRequestType(Choice);
		peerRequest.setRequestData(requestData);
		oos.writeObject(peerRequest);
	}

	public void serverRes() throws ClassNotFoundException, IOException {
		// Response from server
		serverResponse = new Response();
		serverResponse = (Response) ois.readObject();
	}

	/*
	 * 
	 */
	public void retrieve(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// Call ResourceBundle from BaseBacking to get the message.
			baseBacking.getMessage("RET_ID");
			int pid = Integer.parseInt(br.readLine());
			baseBacking.getMessage("RET_HOST");
			String host = br.readLine();
			baseBacking.getMessage("RET_PORT");
			int port = Integer.parseInt(br.readLine());

			/*
			 * we need these five lines to make
			 * our update to the global array files
			 */
			FileInfo file = new FileInfo();
			file.setPeerID(peerID);
			file.setPeerHost(peerHost);
			file.setPortNumber(portNumber);
			file.setFileName(fileName);
			
			//Pass the current file information to PeerClient Class to retrieve it
			new PeerClient(pid, port, host, fileName, peerDir);

			// add to the indexing server and update the current global array files
			serverReq("UPDATE", file);
			serverRes();
			if ((boolean) serverResponse.getResponseData()) {
				baseBacking.getMessage("RET_SUCC");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		// Invoking peer class object
		new Peer();
	}
}