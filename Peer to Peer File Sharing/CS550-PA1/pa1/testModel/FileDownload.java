package testModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import controller.PeerClient;

public class FileDownload extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3228980802218156849L;
	private String peerAddress;
	private String fileName;
    private int port;
	private int NUMBER_OF_TESTs = 100;
	private long startTime, endTime, totalTime, totalFileSize;
	private BufferedReader input;
	private double time, avgSpeed;
	private File file;
	private String DirPath1 = "C:/Users/Hassan Alamri/Desktop/02_Alamri_Hassan_PA1/PA1/Project/files/downloadTest";
	
	public FileDownload(String host, int port, String file) {
		this.peerAddress = host;
        this.port = port;
		this.fileName = file;
		
		totalTime = 0; 
		totalFileSize = 0;
		System.out.println("Test Started...");
		input = new BufferedReader(new InputStreamReader(System.in));	
	}
	
	public long timeCounter(long NUMBER_OF_TESTs) throws IOException {
		for (int i = 0; i < NUMBER_OF_TESTs; i++) {
			startTime = System.currentTimeMillis();
			new PeerClient(0, port, peerAddress, fileName, DirPath1);
			endTime = System.currentTimeMillis();
			totalTime += (endTime - startTime);
			file = new File( DirPath1 +"/"+ fileName);
			totalFileSize += file.length();
			file.delete();
		}
		return totalFileSize;
	}
	
	public void run() {
		try {
			time = (double) Math.round(totalTime / 200.0);
			avgSpeed = (timeCounter(NUMBER_OF_TESTs) / (1024 * 1024)) / time;
			System.out.println("The average speed for downloading [" + NUMBER_OF_TESTs + "] files is (" + avgSpeed + ") MBps.");
			System.out.println("Press Enter button");
			input.readLine();
			this.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}