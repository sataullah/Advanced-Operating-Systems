package common;

import java.io.Serializable;

/*
 * This Class is for file information that we need each time we register, search, update, and deregister files. 
 * The Class is a such Getter and Setter and it is serializable to control thread access.
 */

public class FileInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int peerID;
	private String peerHost;
	private int portNumber;
	private String fileName;

	public int getPeerID() {
		return peerID;
	}

	public void setPeerID(int peerID) {
		this.peerID = peerID;
	}

	public String getPeerHost() {
		return peerHost;
	}

	public void setPeerHost(String peerHost) {
		this.peerHost = peerHost;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
