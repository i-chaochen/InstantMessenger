/**
	 * this method of creating Message and Command
	 * @param command
	 * @param msg
	 * @return
	 */
package com.im.common;

public class Message implements java.io.Serializable{
		

	private static final long serialVersionUID = -1193517304641883987L;
	private int command;//the type of command
	private String getter;//the message getter
	private String sender;//the message sender
	private Object con;//the object of message
	private String sendTime;//message's sending time
	
	public static Message createCommandMessage(int command, String msg)
	{
		Message m = createCommandMessage(command);
		m.setCon(msg);
		return m;
	}
	
	/**
	 * the method of creating Message for command
	 * @param command
	 * @return
	 */
	public static Message createCommandMessage(int command)
	{
		Message m = new Message();
		m.setCommand(command);
		return m;
	}
	
	public String getSender() 
	{
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getGetter() {
		return getter;
	}

	public void setGetter(String getter) {
		this.getter = getter;
	}

	public Object getCon() {
		return con;
	}

	public void setCon(Object con) {
		this.con = con;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	
	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}
}
