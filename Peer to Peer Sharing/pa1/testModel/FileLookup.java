package testModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import common.Request;

public class FileLookup extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6302469642182251023L;
	private String serverAddress;
	private String fileName;
	private int NUMBER_OF_TESTs = 100;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Request peerRequest;
	private long startTime, endTime, totalTime;
	BufferedReader input;
	private double avgTime;
	
	public FileLookup(String host, String file) {
		this.serverAddress = host;
		this.fileName = file;
		socket = null;
		in = null;
		out = null;
		peerRequest = null;
		totalTime = 0;
	}
	
	public long timeCounter(long NUMBER_OF_TESTs) throws IOException {
		totalTime = 0;
		for (int i = 0; i < NUMBER_OF_TESTs; i++) {
			startTime = System.currentTimeMillis();
			peerRequest = new Request();
			peerRequest.setRequestType("SEARCH");
			peerRequest.setRequestData(fileName);
			out.writeObject(peerRequest);
			endTime = System.currentTimeMillis();
			totalTime += (endTime - startTime);
		}
		return totalTime;
	}
	
	public void run() {
		
		try {
			/*
			 * Making connection to the indexing server.
			 * Note thT We should follow the steps in the test document
			 * to get this right.
			 */
			socket = new Socket(serverAddress, 5000);
			input = new BufferedReader(new InputStreamReader(System.in));
	        out = new ObjectOutputStream(socket.getOutputStream());
	        out.flush();
	        in = new ObjectInputStream(socket.getInputStream());
			avgTime = (double) Math.round(timeCounter(NUMBER_OF_TESTs) / (double) NUMBER_OF_TESTs) / 200;
			System.out.println("The average time for [" + NUMBER_OF_TESTs + "] lookup requests is (" + avgTime + ") seconds.");
			input.readLine();
			this.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				/* 
				 * Here we need to close all input and output streams.
				 * because we are facing leak from these streams.
				 */
				if (out != null)
					out.close();
				
				if (in != null)
					in.close();
				
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
