/**
 * the command is for getting online friends
 */
package com.im.server.command;

import com.im.common.Command;
import com.im.common.Message;
import com.im.server.model.ManageClientThread;
import com.im.server.model.MessageTransmitter;

public class GetOnlineFriendsCommand extends AbstCommand implements Command {

	private MessageTransmitter transmitter;
	
	public GetOnlineFriendsCommand(MessageTransmitter transmitter)
	{
		super("GETONLINEFRIENDS");
		this.transmitter = transmitter;
	}
	
	@Override
	public void execute(Message message) throws Exception
	{
		// get the online friends from the server and reply
		String res=ManageClientThread.getAllOnLineUserid();
	
		//the message is for replying the online friends
		Message m2=new Message();
		m2.setCommand(Command.USER_LOGIN_LIST);
		m2.setCon(res);
		m2.setGetter(message.getSender());
		transmitter.transmitToClient(m2);// transmit to client
	}

}
