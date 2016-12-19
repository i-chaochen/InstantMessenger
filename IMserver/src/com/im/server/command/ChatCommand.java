
/**
 * the command is for chat 
 */
package com.im.server.command;

import com.im.common.Command;
import com.im.common.Message;
import com.im.server.model.ManageClientThread;
import com.im.server.model.SerConClientThread;

public class ChatCommand extends AbstCommand implements Command 

{	
	public ChatCommand() 
	{
		super("CHAT");
	}
	
	@Override
	public void execute(Message message) throws Exception 
	{	
		//The server transmit message
		//get the getter thread
		SerConClientThread sc = ManageClientThread.getClientThread(message.getGetter());
		//if user is not login local server,transmit to other sync servers
		if(sc == null)
			{
			//transmit to other servers
			sc = ManageClientThread.getSyncUserThread(message.getGetter());
			}
		if(sc != null)
			{
			//transmit to related clients
			sc.getMessageTransmitter().transmitToClient(message);
			}
	}

}
