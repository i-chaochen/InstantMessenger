/**
 * user login command
 */
package com.im.server.command;

import java.io.IOException;
import java.util.Date;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.RSAEncryption;
import com.im.common.User;
import com.im.server.model.ManageClientThread;
import com.im.server.model.SerConClientThread;
import com.im.server.model.ServerManager;

public class LoginCommand extends AbstCommand implements Command 

{
	private final String DEFAULT_PWD = "12345"; //default password is 12345
	
	private SerConClientThread sc; //the thread of client connecting to server

	public LoginCommand(SerConClientThread sc)
	{
		super("LOGIN");
		this.sc = sc;
	}
	
	@Override
	public void execute(Message message)throws Exception
	{
		User user = (User) message.getCon(); //get the User
		
		String userId = user.getUserId();  //user id
//decrypt 
byte[] pwdByte = user.getPasswd();
String pwd = RSAEncryption.getInstance().decrypt(pwdByte);
			
		boolean isSuccess = false; //login result
		
		Message sendMsg = new Message();
		
		//check user is login
		if(!DEFAULT_PWD.equals(pwd)) //password is error
		{
			isSuccess = false;
			sendMsg.setCommand(Command.LOGIN_FAIL);
		}else{ //password is right
			isSuccess = true;
			sendMsg.setCommand(Command.LOGIN_SUCCEED);
			 }
		
		//to transmit login
		try {
			sc.getMessageTransmitter().transmitToClient(sendMsg);
		} catch (IOException e) {
			e.printStackTrace();
			isSuccess = false;
		}
		//if login is success
		if(isSuccess)
		{
			//user login and keep this client's thread 
			String serverName = ServerManager.getInstance().getLocalServerBean().getServerName();
			
			user.setServerName(serverName);
			
			sc.setOwner(user);
			if(ManageClientThread.addClientThread(user, sc))
			{
				//DO NOT login again
				isSuccess = true;
				user.setIsLogin(true);
				//synchronize the login message to sync servers
				ManageClientThread.syncUserWithServer(user);
			}
		}
			//if login is failed
			else if(!isSuccess)
			{//login is failed, interrupt this connected socket
			 //and close this connection
			if(sc.isAlive())
			{
				sc.interrupt();
			}
			sc.close();
			sc = null;
			}
	}

}
