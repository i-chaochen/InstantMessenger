	/**
	 * the server's connection 
	 */

package com.im.server.model;

import java.net.*;
import java.util.Iterator;
import java.io.*;

import com.im.common.ServerBean;

public class MyImServer extends Thread {

	private ServerSocket serverSocket;
	
	private ServerBean serverBean;
	
	public MyImServer(ServerBean serverBean) 
	{
		this.serverBean = serverBean;
	}
	
	/**
	 * to initialize serverSocket 
	 * @throws Exception
	 */
	public void init() throws Exception
	{
		serverSocket = new ServerSocket(serverBean.getPort());
System.out.println("Server is ready for listening at " + serverBean.getPort());
	}
	
	/**
	 * get server bean
	 * @return
	 */
	public ServerBean getServerBean()
	{
		return this.serverBean;
	}

	/**
	 * close this server
	 */
	public synchronized void stopServer()
	{
		try {
			this.interrupt();
		} finally{
			try {//clear all connected threads
				ManageClientThread.clearClientThread();
				this.serverSocket.close();
			} catch (IOException e) {
			}
			this.serverSocket = null;
		}
	}
	
	/**
	 * building sync server and sync server's message
	 */
	private void syncServer()
	{
System.out.println(" [SYNC] synchronizing with server(s)...");
		//get this local server name
		ServerBean localSb = ServerManager.getInstance().getLocalServerBean();
		String localName = localSb.getServerName();
		//iterate server configuration 
		Iterator<ServerBean> sbIter = ServerManager.getInstance().iteratorServerMap();
		ServerBean sb = null;  // this sb server bean as being iterated
		while(sbIter.hasNext())
		{
			sb = sbIter.next();
			
			if(sb.isLocal())//if is server himself, continue.
				{
				continue;
				}
			
			try {
				//building sync connection
				Socket socket = new Socket(sb.getIp(), sb.getPort());

				SerConClientThread serConClientThread = new SerConClientThread(socket);
				//sending sync message
				serConClientThread.sendSyncConn(localName, sb);
			} catch (Exception e) {
				continue;
			}
		}
	
	}
	
	public void run()
	{
		//put sync server into thread
		syncServer();
		
		try {
			while (true) 
			{
				//waiting connect
				Socket s = serverSocket.accept();

				new SerConClientThread(s).start();
			}
		} catch (Exception e) 
		{
			System.out.println("exception during waiting for connection: " + e);
		} finally {

		}
	}
	
	
}
