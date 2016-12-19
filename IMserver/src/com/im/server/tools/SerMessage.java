/*
 * this message class is for server sending online friends
 */
package com.im.server.tools;

public class SerMessage implements java.io.Serializable {
	
	private String getterS;
	private String senderS;
	private String onlineuid;
	
	public String getGetterS() {
		return getterS;
	}
	public void setGetterS(String getterS) {
		this.getterS = getterS;
	}
	public String getSenderS() {
		return senderS;
	}
	public void setSenderS(String senderS) {
		this.senderS = senderS;
	}
	public String getOnlineuid() {
		return onlineuid;
	}
	public void setOnlineuid(String onlineuid) {
		this.onlineuid = onlineuid;
	}
}
