/*
 * this thread is for server connecting to clients
 */
package com.im.server.model;

import java.net.Socket;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.ServerBean;
import com.im.common.User;
import com.im.server.command.ChatCommand;
import com.im.server.command.GetOnlineFriendsCommand;
import com.im.server.command.LoginCommand;
import com.im.server.command.LogoutCommand;
import com.im.server.command.OkCommand;
import com.im.server.command.SyncConnCommand;
import com.im.server.command.SyncUserCommand;

public class SerConClientThread extends Thread{

	
	/**
	 * the connection thread owner
	 */
	private User ower;
	
	/**
	 * the connection socket
	 */
	private Socket socket;
	
	/**
	 * the message transmitter
	 */
	private MessageTransmitter transmit;
	
	public SerConClientThread(Socket socket)
	{
		this.socket = socket;
		transmit = new MessageTransmitter(socket);
	}
	
	/**
	 * set the method for thread owner
	 * @param user
	 */
	public void setOwner(User user)
	{
		ower = user;
	}
	
	public MessageTransmitter getMessageTransmitter()
	{
		return this.transmit;
	}
	
	/**
	 * close this connection
	 */
	public synchronized void close()
	{
		transmit.closeSocketSafely();
	}
	
	/**
	 * sending the sync message to other sync servers
	 * @param localName
	 * @param sb
	 */
	public void sendSyncConn(String localName, ServerBean sb)
	{
		//set for the local server's user
		User su = new User();
		su.setIsServer(true);
		su.setUserId(localName);
		su.setServerName(sb.getServerName());
		su.setServerIp(sb.getIp());
		su.setPort(sb.getPort());
		su.setConnIp(socket.getInetAddress().getHostAddress());
		// the sync message
		Message m = new Message();
		m.setCommand(Command.SYNC_CONN);
		m.setCon(su);
		
		try {
			//transmit local server's user to sync servers
			transmit.transmitToClient(m);
			
			su.setUserId(sb.getServerName());
			this.setOwner(su);
			if(!ManageClientThread.addSyncThread(su, this))
			{
				throw new Exception("duplicated synchronize conncetion");
			}
			//connection is successful
			new OkCommand();
			//start listen
			this.start();
			
		} catch (Exception e) {
			this.interrupt();
			this.close();
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
//this thread is capable of receiving the information sent from client
			try {
				Message m=(Message)transmit.transmitFromClient();
				//keep sync
				if(m == null)
				{
					continue;
				}
				int type = m.getCommand();
				
				switch(type)
				{
				case Command.LOGIN://to address login
					new LoginCommand(this).execute(m);
					break;
				case Command.GET_ONLINEFRIEND://to address getting online friends
					new GetOnlineFriendsCommand(transmit).execute(m);
					break;
				case Command.COMM_MES://to address sending message
					new ChatCommand().execute(m);
					break;
				case Command.LOGOUT://to address logout
					new LogoutCommand().execute(m);
					break;
				case Command.SYNC_CONN://to address building sync
					new SyncConnCommand(this).execute(m);
					break;
				case Command.SYNC_USER://to address user sent from other sync server
					new SyncUserCommand().execute(m);
					break;
				case Command.SYNC_OK://to address command is successful
					new OkCommand().execute(m);
					break;
					default:
				}
			} catch (Exception e) {
				e.printStackTrace();
System.out.println("user [" + ower + "] logout !");
				// delete cache
				ManageClientThread.removeClientThread(ower);
				//if the connection between other sync server is close
				//delete the ower cache
				if(ower.isServer())
				{
					ManageClientThread.removeSyncThread(ower);
				}
				break;
			}

		}
	}
}
