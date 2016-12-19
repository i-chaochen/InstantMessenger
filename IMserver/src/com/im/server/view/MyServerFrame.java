/*
 * this is Server Frame
 * 1. start server
 * 2. close server
 * 3. control and view users
 */
package com.im.server.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import com.im.common.Utils;
import com.im.server.model.ManageClientThread;
import com.im.server.model.MyImServer;
import com.im.server.model.ServerManager;

public class MyServerFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JPanel jp1;
	private JButton jb1,jb2;
	
	private MyImServer server;

	public static void main(String[] args) 
	{
		new MyServerFrame();
	}

	public MyServerFrame()
	{
		try 
		{
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
		this.getContentPane().setLayout(new BorderLayout());
		
		this.jp1=new JPanel();
		this.jb1=new JButton("Start the Server");
		this.jb1.addActionListener(this);

		this.jb2=new JButton("Stop the Server");
		this.jb2.setEnabled(false);
		this.jb2.addActionListener(this);

		this.jp1.add(this.jb1);
		this.jp1.add(this.jb2);

		this.getContentPane().add(this.jp1, BorderLayout.NORTH);
		
		
		this.setSize(600,300);
		this.setLocation(200,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		
		createServer();
	}

	private void createServer()
	{
		if(server != null)
			{
			return;
			}
		server = ServerManager.getInstance().createServer();
		
		if(server == null)
		{
			JOptionPane.showMessageDialog(this, "No more port(s) available, please check [server.properties] under your folder.");
			this.setVisible(false);
			this.dispose();
			System.exit(-1);
		}
		
		this.setTitle(server.getServerBean().getServerName() + " on " + server.getServerBean().getPort());
	}

	public void actionPerformed(ActionEvent e)
	{

		if(e.getSource()==this.jb1)
		{
			this.createServer();
			server.start();
			this.jb1.setEnabled(false);
			this.jb2.setEnabled(true);

		}
		else if(e.getSource()==this.jb2)
		{
			this.server.stopServer();
			this.server = null;
			this.jb1.setEnabled(true);
			this.jb2.setEnabled(false);
		}

	}

	


}




