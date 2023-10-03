package providers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * PeerServerThread Class is to start a new thread 
 * taking two parameters peerSocket and peerServerDir from PeerServer Class.
 */
public class PeerServerThread implements Runnable {

	private Socket peerSocket;
	private String peerServerDir;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private FileInputStream fis;
	private BufferedInputStream bis;
	private File newFile;
	private String fileName;
	
	/*
	 * A constructor that take two parameters 
	 * to start new thread for peer to act as a server.
	 * 
	 */
    public PeerServerThread(Socket peerSocket, String peerServerDir) {
        this.peerSocket = peerSocket;       
        this.peerServerDir = peerServerDir;
    }

	public void run() {
        try {
        	/*
        	 * instantiating the ObjectOutputStream and ObjectInputStream
        	 * using peerSocket to get files and store it to array list
        	 * as temporary to be ready for registering them.
        	 */
        	oos = new ObjectOutputStream(peerSocket.getOutputStream());
        	ois = new ObjectInputStream(peerSocket.getInputStream());
        	
            // Retrieving file name to download it
            fileName = (String)ois.readObject();
            
            while(true) {
                // Reading file info
                newFile = new File(peerServerDir + "//"+ fileName);
                byte b[] = new byte[(int)newFile.length()];
                
                // Writing file info
                oos.writeObject((int)newFile.length());
                oos.flush();
                
                // Reading file content
                fis = new FileInputStream(newFile);
                bis = new BufferedInputStream(fis);
                bis.read(b, 0, (int)newFile.length());

                // Writing file content
                oos.write(b, 0, b.length);
                oos.flush();                
            }
        }
        catch(Exception e) {
            
        }
    }
}
