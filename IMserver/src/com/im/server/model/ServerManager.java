/*
 *  Reading server's configuration from server.properties
 */
package com.im.server.model;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.im.common.ServerBean;
import com.im.common.Utils;

public class ServerManager 
{	
	//static initializer	
	private static ServerManager serverManager;

	static
	{
		serverManager = new ServerManager();
		serverManager.loadServerConfig();
	}
	
	//keep serverbean in the treemap 
	//and use synchronizedMap to make sure thread safe
	private Map<String, ServerBean> serverMap = Collections.synchronizedMap(new TreeMap<String, ServerBean>());

	
	public static ServerManager getInstance()
	{
		return serverManager;
	}
	
	/**
	 * the method of creating server by server's configuration 
	 * @return
	 */
	public MyImServer createServer()
	{	
		MyImServer server = null;
		ServerBean sb=null;
		boolean isCreated = false;
		
		//iterate server's configuration from iteratorServerMap
		for(Iterator<ServerBean> iter = serverManager.iteratorServerMap(); iter.hasNext();)
		{
			sb = iter.next();
			sb.setLocal(false);

			if(isCreated)
			{	
				continue;
			}

			server = new MyImServer(sb);
			
			try {
				//server is created
				server.init();
				sb.setLocal(true);
				isCreated = true;
			} catch (Exception e) {
				//create server failed, using other server configuration 
				server =null;
				continue;
			}
		}
		
		return server;
	}
	
	/**
	 * iterate server configuration from server map
	 * @return
	 */
	public Iterator<ServerBean> iteratorServerMap()
	{
		return serverMap.values().iterator();
	}
	
	/**
	 * get the local server bean from serverMap
	 * @return
	 */
	public ServerBean getLocalServerBean()
	{
		ServerBean sb = null;

		for(Iterator<ServerBean> iter = serverMap.values().iterator(); iter.hasNext();)
		{
			sb = iter.next();
			if(sb.isLocal())
			{
				return sb;
			}
			sb = null;
		}
		return null;
	}
	
	/**
	 * the method of loading server.properties
	 */
	public void loadServerConfig()
	{
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("server.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String portStr = prop.getProperty("ports");
		
		if(Utils.isNullString(portStr))
			this.loadRealServer(prop);
		else
			this.loadSimulateServer(portStr);
		
	}
	
	/**
	 * the distributed servers on a single computer
	 * @param portStr is the list of server port: 9999 9998 9997
	 */
	private void loadSimulateServer(String portStr)
	{
		if(Utils.isNullString(portStr))
		{
			portStr = String.valueOf(ServerBean.DEFAULT_PORT);
		}
		String[] portStrs = Utils.splitString(" ", portStr); 
		
		int port = 0;
		String serverName = null;
		ServerBean serverBean = null;
		
		for(int i = 0; i < portStrs.length; i++)
		{
			port = Integer.parseInt(portStrs[i]);
			serverName = "localhost" +  (i + 1);
			serverBean = new ServerBean(serverName, "127.0.0.1", port);
			
			serverMap.put(serverName, serverBean);
		}
	}
	
	/**
	 * the distributed servers in different computers
	 * @param prop
	 */
	private void loadRealServer(Properties prop)
	{
		//the local server's port
		String localPort = prop.getProperty("port");
		serverMap.put("localhost", new ServerBean("localhost", "127.0.0.1", Integer.parseInt(localPort)));
		
		//the total of other servers
		int total = Integer.parseInt(prop.getProperty("total"));
		
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
	
}
