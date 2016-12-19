/*
 * IM client login windows
 */
package com.im.client.view;

import java.awt.*;

import com.im.tools.*;
import java.awt.event.*;

import javax.swing.*;
import com.im.client.model.*;
import com.im.common.*;

import java.io.*;
import java.util.Vector;

public class ImClientLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// define the component of North
	JLabel jbl1;

	// define the component of Center
	JPanel jp2;
	JLabel jp2_jbl1, jp2_jbl2, jp2_jbl3;
	JTextField jp2_jtf;
	JPasswordField jp2_jpf;
	JComboBox jp3_jcb;

	// define the component of South
	JPanel jp1;
	JButton jp1jb1;

	public static void main(String[] args) 
	{
		new ImClientLogin();
	}

	public ImClientLogin() 
	{
		try {
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// North
		jbl1 = new JLabel(new ImageIcon("images/title.jpg"));

		// South
		jp1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jp1jb1 = new JButton("LOGIN");
		// respond for client login
		jp1jb1.setPreferredSize(new Dimension(120,40));
		jp1jb1.addActionListener(this);

		// put the button into JPanel
		jp1.add(jp1jb1);

		// Center
		jp2 = new JPanel(new GridLayout(3, 2));

		jp2_jbl1 = new JLabel("User Number", JLabel.CENTER);
		jp2_jbl2 = new JLabel("User Password", JLabel.CENTER);
		jp2_jbl3 = new JLabel("Select Server", JLabel.CENTER);
		jp2_jbl1.setForeground(Color.RED);
		jp2_jbl2.setForeground(Color.RED);
		jp2_jbl3.setForeground(Color.RED);
		jp2_jtf = new JTextField(JLabel.CENTER);
		
		jp2_jpf = new JPasswordField(JLabel.CENTER);
		
		Vector<ServerBean> vector = ServerConfig.getInstance().getServerBeanVector();
		vector.add(0, new ServerBean("Auto", "Auto", 0));
		jp3_jcb = new JComboBox(vector);

		
		// user number
		jp2.add(jp2_jbl1);
		// type user number
		jp2.add(jp2_jtf);
		// user password
		jp2.add(jp2_jbl2);
		// type user password
		jp2.add(jp2_jpf);

		jp2.add(jp2_jbl3);
		jp2.add(jp3_jcb);

		this.add(jbl1, "North");
		this.add(jp2, "Center");
		this.add(jp1, "South");
		this.setTitle("Instant Messenger's Login Window");
		this.setResizable(false);
		this.setSize(400, 300);
		this.setVisible(true);
		this.setLocation(350, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) 
	{
		// if client clicks login
		if (e.getSource() == jp1jb1) 
		{
			login();
		}

	}

	public void login() 
	{
		String userId = jp2_jtf.getText().trim();
		String pwd = new String(jp2_jpf.getPassword());
		if(Utils.isNullString(userId) || Utils.isNullString(pwd))
		{
			JOptionPane.showMessageDialog(this, "User's name or password is empty!");
			return;
		}
		User u = new User();
		u.setUserId(userId);
//encrypt
u.setPasswd(RSAEncryption.getInstance().encrypt(pwd));
		ServerBean sb = (ServerBean) jp3_jcb.getSelectedItem();

		ImClientConServer con = new ImClientConServer(sb, u);
		
		if (con.login()) 
		{
			try {
				// put friends list in here
				ImFriendsList imList = new ImFriendsList(con, u);
				ManageImFriendsList.addImFriendsList(u.getUserId(), imList);

				// send a request for online friends
				ObjectOutputStream oos = new ObjectOutputStream(con.getSocket().getOutputStream());

				// a message package
				Message m = new Message();
				m.setCommand(Command.GET_ONLINEFRIEND);
				// set I want this userId's online-friends
				m.setSender(u.getUserId());
				oos.writeObject(m);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// close the login windows
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this,
					"User's name or password are error!");
		}
	}

}
