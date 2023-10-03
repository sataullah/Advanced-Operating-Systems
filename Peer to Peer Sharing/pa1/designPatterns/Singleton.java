package designPatterns;

import java.util.ArrayList;

import common.FileInfo;

/*
 * This is a Singleton Class which is a design patter.
 * The class makes or represents a single instance or object through whole project.
 * We need this kind of design patter in which each peer and the indexing server can make changes 
 * to one instance, array, or peerCount variables among the others and keep the original value.
 */

public class Singleton {
	// One object among the whole project.
	private static Singleton singletonObj = new Singleton();
	// One global file Array among the whole project.
	private static ArrayList<FileInfo> arrayFiles = new ArrayList<FileInfo>();
	// count number of each connected peer among the whole project.
	private static int peerCount;

	public Singleton() {
		// TODO Auto-generated constructor stub
	}

	public static Singleton getInstance() {
		return singletonObj;
	}

	public int getPeerCount() {
		return peerCount;
	}

	public void setPeerCount(int peerCount) {
		Singleton.peerCount = peerCount;
	}

	public ArrayList<FileInfo> getArrayFiles() {
		return arrayFiles;
	}

	public void setArrayFiles(ArrayList<FileInfo> arrayFiles) {
		Singleton.arrayFiles = arrayFiles;
	}

}
