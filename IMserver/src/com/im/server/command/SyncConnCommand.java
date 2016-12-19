/**
 * to execute sync connection command
 *
 */
package com.im.server.command;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.User;
import com.im.server.model.ManageClientThread;
import com.im.server.model.SerConClientThread;


public class SyncConnCommand extends AbstCommand implements Command 
{
	private SerConClientThread sc;
	
	public SyncConnCommand(SerConClientThread sc)
	{
		super("SYNCCONNECTION");
		this.sc = sc;
	}
	
	@Override
	public void execute(Message message) throws Exception 
	{
		User user = (User) message.getCon();

		sc.setOwner(user);
		//add the sync thread in cache
		if(ManageClientThread.addSyncThread(user, sc))
		{
			Message sendMsg = Message.createCommandMessage(SYNC_OK, user + " sync connection established!");
			sc.getMessageTransmitter().transmitToClient(sendMsg);
			new OkCommand().execute(sendMsg);
		}
	}

}
