/*
 * it is the chat window
 * because Client has to be listened, client needs a thread
 */
package com.im.client.view;
import javax.swing.*;

import com.im.tools.*;
import com.im.client.model.*;
import com.im.common.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class ImChat extends JFrame implements ActionListener, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	ImClientConServer client;
	User owner;
	JTextArea jta,jta2;

	JButton jb;
	JPanel jp;
	String friendId;
	
	
	public ImChat(ImClientConServer client, User owner,String friend)
	{	
		this.client=client;
		this.owner=owner;
		this.friendId=friend;
		jta=new JTextArea();
		jta.setBackground(Color.YELLOW);
jta.setLineWrap(true);
jta.setEditable(false);
jta.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jta2=new JTextArea();
jta2.setLineWrap(true);
jta2.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
 
		jb=new JButton("Sending Message");
		//listen this send button
		jb.addActionListener(this);
		//listen keyboard
		jta2.addKeyListener(this);
		
		//the south of JPanel(JTextArea + JButton) changes as GridLayout
		jp=new JPanel(new GridLayout(1,2,2,2));
		jp.add(jta2);
		
		jp.add(jb);
		
		this.setIconImage((new ImageIcon("images/head3.jpg").getImage()));
		this.add(jta,"Center");
		this.add(jp,"South");
		this.setSize(400,400);
this.setTitle("No."+owner.getUserId()+": You are talking with No."+friend);
		this.setVisible(true);
}
	


	//a method for showing information
	public void showMessage(Message m)
	{
//decrypt message
String msg = RSAEncryption.getInstance().decrypt((byte[]) m.getCon());
		String info="No."+m.getSender()+" says "+"\r\n"+msg+"\r\n"+"to No."+m.getGetter()+"\r\nat "+m.getSendTime()+"\r\n";
		this.jta.append(info);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==jb)
		{
			send();
		}
	}
	
	private void send() 
	{		//get the text from jta2
			String text=jta2.getText();
			//if the text is null string, return
		if(Utils.isNullString(text))
			{
				return;
			}
			//message to be sent
			Message m=new Message();
			m.setCommand(Command.COMM_MES);
			m.setSender(this.owner.getUserId());
			m.setGetter(this.friendId);
			m.setCon(text);
			m.setSendTime(Utils.formatDate(new Date()));
			String info="No."+m.getSender()+" says "+"\r\n"+m.getCon()+"\r\n"+"to No."+m.getGetter()+"\r\nat "+m.getSendTime()+"\r\n";
			this.jta.append(info);
//encrypt
m.setCon(RSAEncryption.getInstance().encrypt(text));
			
			//sending to server
			try {
				ObjectOutputStream oos=new ObjectOutputStream(client.getSocket().getOutputStream());
				oos.writeObject(m);
				jta2.setText(null);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	

	public void keyPressed(KeyEvent e) {
		//if client press shift, sending message
		if((e.getKeyCode()==(KeyEvent.VK_ALT)))
		{
			send();
		}
	}
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
/*	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			//accept and read
			try {
				ObjectInputStream ois=new ObjectInputStream(ImClientConServer.s.getInputStream());
				//crate a message to read
				Message m=(Message)ois.readObject();
		
				//show up
			String info="No."+m.getSender()+" says "+m.getCon()+"\r\n"+" to No."+m.getGetter()+"\r\n"+" at "+m.getSendTime()+"\r\n";
			this.jta.append(info);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

}








