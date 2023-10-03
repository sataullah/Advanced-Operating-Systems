package common;

import java.io.Serializable;

/*
 * This Class is for response that we need each time we register, search, update, and deregister files from Server to Client or Peer. 
 * The Class is a such Getter and Setter and it is serializable to control thread access.
 * Moreover, request Type holds words like register, search, update, or deregiste, and behave and act accordingly.
 * And for request Data holds an object or array of objects. Each object holds file information.
 */

public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object responseData;

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
}
