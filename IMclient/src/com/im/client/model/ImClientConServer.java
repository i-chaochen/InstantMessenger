/*
 * it is the part of client to connect server
 */

package com.im.client.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.ServerBean;
import com.im.common.User;
import com.im.tools.ClientConServerThread;
public class ImClientConServer {

	private User owner;
	private Socket socket;
	private ServerBean serverBean;
	private ClientConServerThread ccsThread;

	public ImClientConServer(ServerBean sb, User user)
	{
		this.serverBean = sb;
		this.owner = user;
		this.connectServer();
	}
	
	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket s) {
		this.socket = s;
	}

	public ClientConServerThread getCcsThread() {
		return ccsThread;
	}


	public void connectServer()
	{
		if(this.serverBean != null && !this.serverBean.getServerName().equals("Auto"))
		{
			try {
				this.socket=new Socket(serverBean.getIp(),serverBean.getPort());
				return;
			} catch (Exception e) {
				System.out.println("failed to connceted server: " +e);
			}
		}
		
		ServerConfig sc = ServerConfig.getInstance();
		ServerBean sb = null;
		
		for(Iterator<ServerBean> iterator = sc.iterator();iterator.hasNext();)
		{
			sb = iterator.next();
			
			try {
				this.socket=new Socket(sb.getIp(),sb.getPort());
				serverBean = sb;
				break;
			} catch (Exception e) {
				serverBean = null;
				System.out.println("failed to connceted server: " +e);
			}
		}
	}
	
	//sending the first request
	public boolean login()
	{
		boolean ret = false;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		try
		{
			if(socket == null)
				return ret;
			
			Message m = new Message();
			m.setCommand(Command.LOGIN);
			m.setCon(owner);
			
			owner.setConnIp(socket.getInetAddress().getHostName());
			owner.setServerIp(serverBean.getIp());
			owner.setPort(serverBean.getPort());
			
			oos = new ObjectOutputStream(this.socket.getOutputStream());
			oos.writeObject(m);

			ois = new ObjectInputStream(this.socket.getInputStream());

			Message ms=(Message)ois.readObject();
			//check the user login
			if(ms.getCommand() == Command.LOGIN_SUCCEED)
			{
				// create a imId and server to keep connection thread
				ccsThread=new ClientConServerThread(this, this.socket);
				//start this connection thread
				ccsThread.start();
				ret=true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			ccsThread.closeSocket();
		}
		finally
		{
		}
		return ret;
	}
}



