/*
 * IM Friends' List, Black's List
 */
package com.im.client.view;
import java.awt.*;

import com.im.client.model.ImClientConServer;
import com.im.common.*;
import com.im.tools.*;
import java.awt.event.*;

import javax.swing.*;

public class ImFriendsList extends JFrame implements ActionListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImClientConServer client = null;
	//make the first card(friends list)
	JPanel jpf1,jpf2,jpf3;
	JButton jpf_jb1,jpf_jb2;
	JScrollPane jsp1;
	private User owner;
	//make the second card(the black list)
	JPanel jpbl,jpbl2,jpbl3;
	JButton jpbl_jb1,jpbl_jb2;
	JScrollPane jsp2;
	JLabel []jbls;
	//change JFrame as CardLayout
	CardLayout cl;

	//update the online friends-list
	public void updateFriends(Message m)
	{
		String onLineFriends[]=m.getCon().toString().split(" ");
		boolean isEnable = false;
		if(m.getCommand() == Command.USER_LOGIN_LIST)
			{
			isEnable = true;
			}
		
		for(int i=0;i<onLineFriends.length;i++)
		{
			
			jbls[Integer.parseInt(onLineFriends[i])-1].setEnabled(isEnable);
		}
	}
	
	public ImFriendsList(ImClientConServer client, User user)
	{	
		this.client = client;
		this.owner=user;
		//make the first card(show up friends list)
		jpf_jb1=new JButton("My Friends List");
		jpf_jb2=new JButton("The Strangers");
		jpf_jb2.addActionListener(this);
		jpf1=new JPanel(new BorderLayout());
		
		// assuming 50 friends
		jpf2=new JPanel(new GridLayout(50,1,5,5));
		//give jpf2 50 friends
		jbls =new JLabel[50];
		
		for(int i=0;i<jbls.length;i++)
		{
			jbls[i]=new JLabel(i+1+"",new ImageIcon("images/head2.jpg"),JLabel.LEFT);
			jbls[i].setEnabled(false);
			if(jbls[i].getText().equals(user.getUserId()))
			{
				jbls[i].setEnabled(true);
			}
			jbls[i].addMouseListener(this);
			jpf2.add(jbls[i]);
		}
		
		jpf3=new JPanel(new GridLayout());
		
		//put the buttons into jpf3 
		jpf3.add(jpf_jb2);
		
		jsp1=new JScrollPane(jpf2);
	
		
		//make jpf1
		jpf1.add(jpf_jb1,"North");
		jpf1.add(jsp1,"Center");
		jpf1.add(jpf3,"South");
		
		//make the second card(the black list)
		jpbl_jb1=new JButton("My Friends List");
		jpbl_jb2=new JButton("The Strangers");
		jpbl_jb1.addActionListener(this);
		jpbl=new JPanel(new BorderLayout());
		
		// assuming 30 people in the black list
		jpbl2=new JPanel(new GridLayout(30,1,5,5));
		//give jpbl2 30 friends
		JLabel []jbls2=new JLabel[30];
		
		for(int i=0;i<jbls2.length;i++)
		{
			jbls2[i]=new JLabel(i+1+"",new ImageIcon("images/head.jpg"),JLabel.LEFT);
			jpbl2.add(jbls2[i]);
		}
		
		jpbl3=new JPanel(new GridLayout(1,1));
		
		//put the buttons into jpf3 
		jpbl3.add(jpbl_jb2);
		
		jsp2=new JScrollPane(jpbl2);
	
		
		//make jpbl
		jpbl.add(jpbl_jb1,"North");
		jpbl.add(jsp2,"Center");
		jpbl.add(jpbl3,"South");
		
		
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(jpf1,"1");
		this.add(jpbl,"2");
		this.setTitle("My Friends");
		//show up owner's ID
		this.setTitle("No."+owner.getUserId());
		this.setSize(200,550);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		//if click the button of the black list
		if(e.getSource()==jpf_jb2){
			cl.show(this.getContentPane(),"2");
		}else if(e.getSource()==jpbl_jb1)
		{
			cl.show(this.getContentPane(),"1");
		}
		}

	
	// if double-clicks friends' image
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()==2)
		{
			String friendNo=((JLabel)e.getSource()).getText();
			
			ImChat imChat=new ImChat(client, this.owner,friendNo);
			
			//add chat window in manage class
			ManageImChat.addImChat(this.owner.getUserId()+"^"+friendNo, imChat);
			//Thread t=new Thread(imChat);
			//t.start();
		}
		
	}

	
	public void mouseEntered(MouseEvent e) {
		JLabel jl=(JLabel)e.getSource();
		jl.setForeground(Color.PINK);
	}

	
	public void mouseExited(MouseEvent e) {
		JLabel jl=(JLabel)e.getSource();
		jl.setForeground(Color.black);
		
	}

	
	public void mousePressed(MouseEvent e) {
		
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
		
	}

	
		
	
}

