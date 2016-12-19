/*
 *  it's the user information 
 */
package com.im.common;

import java.util.Date;

public class User implements java.io.Serializable{
	

	private static final long serialVersionUID = -7688387197731612412L;
	
	private boolean isServer;  //is local or sync server
	private boolean isLogin;	// is login or logout
	
	private String userId;	//user's id
	private byte[] passwd;	//user's password
	private String connIp;	//user's connected ip
	private int port;			//user's connected port
	private Date connDate = new Date();		//user's connected date

	private String serverIp;	//user connected server's ip
	private String serverName;	//user connected server's name
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public byte[] getPasswd() {
		return passwd;
	}
	public void setPasswd(byte[] passwd) {
		this.passwd = passwd;
	}
	public String getConnIp() {
		return connIp;
	}
	public void setConnIp(String connIp) {
		this.connIp = connIp;
	}
	public Date getConnDate() {
		return connDate;
	}
	public void setConnDate(Date connDate) {
		this.connDate = connDate;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setIsLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public boolean isServer() {
		return isServer;
	}
	public void setIsServer(boolean isServer) {
		this.isServer = isServer;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String toString(){
		return userId + " (from " + connIp + ":" + port + " at " + Utils.formatDate(connDate) + ")";
	}
}
