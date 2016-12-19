/*
 * this abstract class is for all others commands to extend
 */
package com.im.server.command;

public abstract class AbstCommand 

{		
	protected String command;
	
	protected AbstCommand(String command)
	{
		this.command=command;
	}
	
	public String getCommand()
	{
		return command;
	}
	
	public String toString()
	{
		return "[" + command +"]";
	}
}
