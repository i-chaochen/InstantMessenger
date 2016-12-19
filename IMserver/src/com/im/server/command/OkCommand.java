/**
 * the command is executed successfully
 *
 */

package com.im.server.command;

import com.im.common.Command;
import com.im.common.Message;


public class OkCommand extends AbstCommand implements Command 

{
	public OkCommand()
	{
		super("OK");
	}
	
	@Override
	public void execute(Message message) throws Exception 
	{
System.out.println(toString() + message.getCon());
	}

}
