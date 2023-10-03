package providers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import common.BaseBacking;
import common.FileInfo;
import common.Request;
import common.Response;
import designPatterns.Singleton;

/*
 * ServerThread Class is acting as central server that contact 
 * all peers at the same time by using request-response objects
 * to contact all peers.  
 * This Class has five main responses to peers:
 * 1- register
 * 2- search
 * 3- update
 * 4- deregister
 * 5- close connection
 */
public class ServerThread implements Runnable {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private ArrayList<FileInfo> allFiles;
	private ArrayList<FileInfo> fileArray = new ArrayList<FileInfo>();
	private int peerID;
	private Response serverResponse;
	private BaseBacking baseBacking;
	private Request peerRequest;
	private String requestType;

	public ServerThread(Socket peerSocket, ArrayList<FileInfo> arrayFiles) {
		this.socket = peerSocket;
		this.allFiles = arrayFiles;
		baseBacking = new BaseBacking();
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			
			/*
			 * Important Note: When you run the test file, commit the below line to avoid
			 * java.lang.ClassCastException
			 */
			peerID = (int) ois.readObject(); //<--This line

			// we have open while loop to keep the connection active
			while (true) {
				try {
					peerRequest = (Request) ois.readObject();
					requestType = peerRequest.getRequestType();

					/*
					 * When the request comes from a peer, it will holds two things: 1- the request
					 * type such as searching 2- the arraylist, or a file as an object, or nothing
					 * for the closing connection.
					 */
					// If the requestType = REGISTER, register all files of the current peer
					if (requestType.equalsIgnoreCase("REGISTER")) {
						fileArray = (ArrayList<FileInfo>) peerRequest.getRequestData();
						register(fileArray);
						responseToPeer(true);
						System.out.println("Peer " + peerID + " has registered " + fileArray.size() + " files");
						// Call ResourceBundle from BaseBacking to get the message.
						baseBacking.getMessage("FILES_NUM", allFiles.size());
					}
					// If the requestType = SEARCH, get the file by its name and send its
					// information back to the peer
					else if (requestType.equalsIgnoreCase("SEARCH")) {
						ArrayList<FileInfo> result = search((String) peerRequest.getRequestData());
						responseToPeer(result);
					}
					// If the requestType = UPDATE, update the arraylist and add the new file to the
					// current peer information
					else if (requestType.equalsIgnoreCase("UPDATE")) {
						FileInfo newFile = (FileInfo) peerRequest.getRequestData();
						update(newFile);
						responseToPeer(true);
						System.out.println("Peer " + peerID + " has registered a new file : " + newFile.getFileName());
						baseBacking.getMessage("FILES_NUM", allFiles.size());
					}
					// If the requestType = DEREGISTER, remove a file using its name and peerID
					else if (requestType.equalsIgnoreCase("DEREGISTER")) {
						ArrayList<FileInfo> result = deregister(peerID, (String) peerRequest.getRequestData());
						responseToPeer(result);
						System.out.println("Peer " + peerID + " has deregistered " + result.size() + " files");
						// Call ResourceBundle from BaseBacking to get the message.
						baseBacking.getMessage("FILES_NUM", allFiles.size());
					}
					// If the requestType = DISCONNECT, Close connection
					else if (requestType.equalsIgnoreCase("DISCONNECT")) {
						// Close the connection and then stop the thread
						socket.close();
						System.out.println("PeerID " + peerID + " disconnected");
						Singleton.getInstance().setPeerCount(Singleton.getInstance().getPeerCount() - 1);
						System.out.println("The number of connected peers : " + Singleton.getInstance().getPeerCount());
						Thread.currentThread().interrupt();
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void responseToPeer(Object responseData) throws IOException {
		// Response to peer
		serverResponse = new Response();
		serverResponse.setResponseData(responseData);
		oos.writeObject(serverResponse);
	}

	public void register(ArrayList<FileInfo> fileArray) throws Exception {
		// Registering files at the server
		for (int i = 0; i < fileArray.size(); i++)
			allFiles.add(fileArray.get(i));
	}

	public ArrayList<FileInfo> search(String fileName) {
		/*
		 * Searching for a file and return its information to a peer. This function is
		 * to get all peers that have the same file name. Collect them to an array of
		 * files and send them back to a peer
		 */
		ArrayList<FileInfo> result = new ArrayList<FileInfo>();
		// Call ResourceBundle from BaseBacking to get the message.
		baseBacking.getMessage("SEARCH");
		for (int i = 0; i < allFiles.size(); i++) {
			FileInfo file = allFiles.get(i);
			if (file.getFileName().equals(fileName)) {
				result.add(file);
			}
		}
		return result;
	}

	public void update(FileInfo newFile) throws Exception {
		// Update the current arraylist files at the server
		allFiles.add(newFile);
	}

	public ArrayList<FileInfo> deregister(int peerID, String fileName) {
		// Deregister or delete a file from indexing server registry and decrease the
		// registry files number
		// Call ResourceBundle from BaseBacking to get the message.
		baseBacking.getMessage("DEREGISTER");
		ArrayList<FileInfo> result = new ArrayList<FileInfo>();
		FileInfo file;
		for (int i = 0; i < allFiles.size(); i++) {
			file = allFiles.get(i);
			if (file.getPeerID() == peerID && file.getFileName().equals(fileName)) {
				result.add(file);
				allFiles.remove(i);
				i--;
			}
		}
		return result;
	}
}
