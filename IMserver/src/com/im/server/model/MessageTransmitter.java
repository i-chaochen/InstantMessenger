/**
 *	this is for transmitting message, encapsulate connected socket
 */
package com.im.server.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.im.common.Message;


public class MessageTransmitter 

{	
	private Socket socket;
	
	public MessageTransmitter(Socket socket)
	{
		this.socket = socket;
	}
	
//the method of ObjectOutputStream
	private ObjectOutputStream getObjectOutputStream() throws IOException
	{
		if(socket == null) 
		{
			throw new IOException("socket connection doesn't establish");
		}
		return new ObjectOutputStream(socket.getOutputStream());
	}
//the method of ObjectInputStream
	private ObjectInputStream getObjectInputStream() throws IOException
	{
		if(socket == null) 
		{
			throw new IOException("socket connection doesn't establish");
		}
		return new ObjectInputStream(socket.getInputStream());
	}
	
	/**
	 * transmit message to related clients and sync servers
	 * @param message
	 * @throws IOException
	 */
	public void transmitToClient(Message message) throws IOException
	{
		ObjectOutputStream os = getObjectOutputStream();
System.out.println(">>>>>write client this message, receiver: " + message.getGetter() + ", sender: " + message.getSender() + ", says: " + message.getCon());
		os.writeObject(message);
		os.flush();
	}
	
	/**
	 * read the message sending to local server
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Message transmitFromClient() throws IOException, ClassNotFoundException
	{
		ObjectInputStream is = getObjectInputStream();
		Message message = (Message) is.readObject();
		if(message != null)	
		{
System.out.println("<<<<<received message from " + message.getSender() + ", to " + message.getGetter() + ", says: " + message.getCon());
		}
			return message;
	}
	
	
	/**
	 * safely close the socket
	 */
	public void closeSocketSafely()
	{
		try
		{
			if (this.socket != null)
			{
				this.socket.close();
				this.socket = null;
			}
		}
		catch (IOException e1)
		{
			// do nothing
		}
	}
}
