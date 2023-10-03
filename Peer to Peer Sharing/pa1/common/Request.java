package common;

import java.io.Serializable;

/*
 * This Class is for request that we need each time we register, search, update, and deregister files from Client or Peer to Server. 
 * The Class is a such Getter and Setter and it is serializable to control thread access.
 * Moreover, request Type holds words like register, search, update, or deregiste, and behave and act accordingly.
 * And for request Data holds an object or array of objects. Each object holds file information.
 */

public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String requestType;
	private Object requestData;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Object getRequestData() {
		return requestData;
	}

	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}
}
