package com.im.client.model;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import com.im.common.ServerBean;

public class ServerConfig {

	private static ServerConfig config;
	private int total; //the total of servers
	private Map<String, ServerBean> serverMap = Collections.synchronizedMap(new TreeMap<String, ServerBean>());
	
	public static ServerConfig getInstance()
	{
		if(config == null)
		{
		config = new ServerConfig();
		}
		return config;
	}
	
	public ServerConfig()
	{
		this.initialize();
	}
	
	private void initialize()
	{
		Properties prop = new Properties();
		try {// load client.properties
			prop.load(getClass().getClassLoader().getResourceAsStream("client.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//the total of servers
		total = Integer.parseInt(prop.getProperty("total"));
		
		String serverName = null;
		String ip = null;
		int port = 0;
		for(int i = 1; i <= total; i++)
		{
			serverName = "server" + i;
			ip = prop.getProperty(i + ".server.ip");
			port = Integer.parseInt(prop.getProperty(i + ".server.port"));
			
			serverMap.put(serverName, new ServerBean(serverName, ip, port));
		}
	}
	
	public Iterator<ServerBean> iterator()
	{
		return serverMap.values().iterator();
	}
	
	public Vector<ServerBean> getServerBeanVector()
	{
		return new Vector<ServerBean>(serverMap.values());
	}
	
	
}
