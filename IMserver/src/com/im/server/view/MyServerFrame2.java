/*
 * this is Server Frame
 * 1. start server
 * 2. close server
 * 3. control and view users
 */
package com.im.server.view;

import javax.swing.*;

import com.im.server.model.MyImServer;
import com.im.server.model.MyImServer2;

import java.awt.*;
import java.awt.event.*;

public class MyServerFrame2 extends JFrame implements ActionListener{

	JPanel jp1;
	JButton jb1,jb2;
	
	public static void main(String[] args) {
		MyServerFrame2 msf=new MyServerFrame2();

	}

	public MyServerFrame2()
	{	
		try {
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jp1=new JPanel();
		jb1=new JButton("Start the Server2");
		jb1.addActionListener(this);
		jb1.setPreferredSize(new Dimension(200,50));
		
		jb2=new JButton("Stop the Server2");
		jb2.setPreferredSize(new Dimension(200,50));
		jb2.addActionListener(this);
		
		jp1.add(jb1);
		jp1.add(jb2);
		
		this.add(jp1);
		this.setSize(500,200);
		this.setLocation(550,400);
		this.setVisible(true);
		this.setTitle("The Server2");
        this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource()==jb1)
		{
			new MyImServer2().start();
			
		}else if(e.getSource()==jb2){
			this.dispose();
		}
	
		
	
	}

	
	}
	



