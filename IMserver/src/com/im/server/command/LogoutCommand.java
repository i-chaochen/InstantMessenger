/**
 * the user logout command
 */
package com.im.server.command;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.User;
import com.im.server.model.ManageClientThread;

public class LogoutCommand extends AbstCommand implements Command 

{
	public LogoutCommand()
	{
		super("LOGOUT");
	}
	
	@Override
	public void execute(Message message) throws Exception 
	{
		User user = (User) message.getCon();
System.out.println(toString() + "user " + user + " logout.");
		ManageClientThread.removeClientThread(user);
	}

}
