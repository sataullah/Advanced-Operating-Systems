package common;

import java.io.Serializable;
/*
 * This class is representing the message of the whole project output messages.
 * It connect each message with a unique key.
 * It retrieves a message from messages_en.properties file which resedents at resources package.
 */
import java.util.ResourceBundle;

public class BaseBacking implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceBundle bundle;
	/*
	 * Here we use Eclipse ResourceBundle Editor.
	 * Or you can say we use an abstract class ResourceBundle java code to connect messages to their keys.
	 * We need such classes to make the code easier and cleaner from filled or stuffed lines.
	 * Eclipse has plugin for editing Java resource bundles. 
	 * It lets you manage all localized (.properties) files in one screen.
	 * You can visit the website for more details.
	 * http://essiembre.github.io/eclipse-rbe/
	 */
	public BaseBacking() {
		bundle = ResourceBundle.getBundle("resources.messages_en");
	}

	//Getter methods to print messages according to the given key String.
	public void getMessage(String msgKey) {
		System.out.print(bundle.getString(msgKey));
	}

	public void getMessage(String msgKey, int arg) {
		System.out.println(bundle.getString(msgKey) + arg);
	}

	public void getMessage(String msgKey, String arg) {
		System.out.println(bundle.getString(msgKey) + arg);
	}

	public void getMessage(String msgKey1, int arg1, String msgKey2, String arg2, String msgKey3, int arg3) {
		System.out.println(bundle.getString(msgKey1) + "(" + arg1 + ")" + " " + bundle.getString(msgKey2) + "[" + arg2
				+ "]" + " " + bundle.getString(msgKey3) + "(" + arg3 + ")");
	}
}
