/**
 *	to execute sync user message
 */
package com.im.server.command;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.User;
import com.im.server.model.ManageClientThread;

public class SyncUserCommand extends AbstCommand implements Command 

{
	public SyncUserCommand() 
	{
		super("SYNCUSER");
	}

	@Override
	public void execute(Message message) throws Exception 
	{
		User user = (User) message.getCon();
		
		//is user login or logout
		String isLogin = user.isLogin() ? "login" : "logout";
		
		if(user.isLogin())
		{//user login
			ManageClientThread.addSyncUser(user);
		}else {	//user logout
			ManageClientThread.removeSyncUser(user);
			  }
	}

}
