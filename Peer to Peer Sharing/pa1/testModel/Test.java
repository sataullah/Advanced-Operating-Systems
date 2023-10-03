package testModel;

import java.io.*;
import common.BaseBacking;

public class Test {
	private static BaseBacking baseBacking;
	private BufferedReader input;
	private String hostAddress, fileName;
	private int port;
	String file1, file2;
	int option = 0;

	public Test() {
		input = null;
		baseBacking = new BaseBacking();
		startTest();

	}

	public void startTest() {

		try {
			input = new BufferedReader(new InputStreamReader(System.in));
			baseBacking.getMessage("TEST_OPTIONS");

			// Check for entered numbers only for the service option.
			try {
				option = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				System.out.println("Error service number!");
				System.exit(0);
			}

			switch (option) {
			case 1:
				baseBacking.getMessage("ENTER_HOST");
				hostAddress = input.readLine();
				baseBacking.getMessage("ENTER_FILE");
				fileName = input.readLine();
				(new FileLookup(hostAddress, fileName)).start();
				Thread.sleep(2000);
				(new FileLookup(hostAddress, fileName)).start();
				Thread.sleep(2000);
				(new FileLookup(hostAddress, fileName)).start();
				Thread.sleep(2000);
				(new FileLookup(hostAddress, fileName)).start();
				Thread.sleep(2000);
				break;

			case 2:
				baseBacking.getMessage("ENTER_HOST");
				hostAddress = input.readLine();
				baseBacking.getMessage("ENTER_PORT");
				port = Integer.parseInt(input.readLine());
				baseBacking.getMessage("ENTER_FILE1");
				file1 = input.readLine();
				baseBacking.getMessage("ENTER_FILE2");
				file2 = input.readLine();
				(new FileDownload(hostAddress, port, file1)).start();
				Thread.sleep(2000);
				(new FileDownload(hostAddress, port, file2)).start();
				Thread.sleep(2000);

				break;

			default:
				System.out.println("Wrong choice. Try again!");
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Test();
	}

}
