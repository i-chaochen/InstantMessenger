package com.im.common;

/*
 * server bean
 *
 */
public class ServerBean 
{
	
	/*
	 * the default connected socket
	 */
	public static final int DEFAULT_PORT = 9999;
	
	private String serverName;	//server name
	private String ip;	// server ip
	private int port = DEFAULT_PORT;	//server port
	private boolean isLocal;	//the server is local server or not 
	
	public ServerBean(String name, String ip, int port) 
	{
		this.serverName = name;
		this.ip = ip;
		this.port = port;
	}

	public String getServerName() 
	{
		return serverName;
	}

	public void setServerName(String serverName) 
	{
		this.serverName = serverName;
	}

	public String getIp() 
	{
		return ip;
	}

	public void setIp(String ip) 
	{
		this.ip = ip;
	}
	
	public int getPort() 
	{
		return port;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}

	public boolean isLocal() 
	{
		return isLocal;
	}

	public void setLocal(boolean isLocal) 
	{
		this.isLocal = isLocal;
	}
	
	public String toString()
	{
		return this.serverName + " [" + this.ip + ":" + this.port + "]";
	}
	
}
