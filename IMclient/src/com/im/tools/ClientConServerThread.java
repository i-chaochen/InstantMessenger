/*
 * the thread is the client and server to keep connection
 */
package com.im.tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.im.client.model.ImClientConServer;
import com.im.client.view.ImChat;
import com.im.client.view.ImFriendsList;
import com.im.common.Message;
import com.im.common.Command;
public class ClientConServerThread extends Thread{

	private Socket socket;
	private ImClientConServer client;
	

	public ClientConServerThread(ImClientConServer client, Socket s)
	{
		this.client = client;
		this.socket=s;
	}
	
	public void closeSocket()
	{
		try
		{
			if (this.socket != null)
			{
				this.socket.close();
				this.socket = null;
				client.setSocket(null);
			}
		}
		catch (IOException e1)
		{
			// do nothing
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			//keep read the message sent from server
			try {
				ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
				Message m=(Message)ois.readObject();
System.out.println("read this message sent from server:" + m);
				if(m.getCommand() == Command.COMM_MES)
				{
					//show the information sent from server to chat window
					ImChat imChat=ManageImChat.getImChat(m.getGetter()+"^"+m.getSender());
					//show up
					imChat.showMessage(m);
				}else if
				(m.getCommand() == Command.USER_LOGIN_LIST || m.getCommand() == Command.USER_LOGOUT_LIST)
				{
System.out.println("Client receives "+m.getCon());
					String getter=m.getGetter();
					
					//change friends-list
					ImFriendsList imFriendsList=ManageImFriendsList.getImFriendsList(getter);

					//update friends-list
					if(imFriendsList!=null)
					{
						imFriendsList.updateFriends(m);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
System.out.println("error occur during waiting for server: " + e);
				closeSocket();
				//client keep connect server
				client.connectServer();
				if(!client.login())
					{
					System.exit(-1);
					}
					break;
			}
		}
	}
}
