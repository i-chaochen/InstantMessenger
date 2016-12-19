package com.im.server.model;

import com.im.common.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class MyImServer2 extends Thread{

	/**
	 * @param args
	 */
	public MyImServer2()
						{
		MyImServer2 imserver2=new MyImServer2();
						}
	//server2 listen at 8888 port
	public void run(){
		try {
			//listen at port 8888
			ServerSocket ss = new ServerSocket(8888);
			
			System.out.println("I am the Server2, listening at the port 8888 ");
			
			while (true) {
				// waiting connect
				Socket cs = ss.accept();

				// received data from client
				ObjectInputStream sois = new ObjectInputStream(cs.getInputStream());
				
				//
				
				//this is for client connected
				User u = (User) sois.readObject();
				
				//create a message sending to client
				Message m = new Message();
				ObjectOutputStream oos = new ObjectOutputStream(cs.getOutputStream());

				if (u.getUserId().equals("1") && u.getPasswd().equals("12345")) 
				{
					// success login information package
					m.setMesType("1");
					oos.writeObject(m);

					// open a thread, which can connect with this client
					SerConClientThread scct = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct);

					// start this thread with client
					scct.start();
					
					//notify other online friends
					scct.notifyOther(u.getUserId());

				} else if (u.getUserId().equals("2")&& u.getPasswd().equals("12345")) {
					m.setMesType("1");
					oos.writeObject(m);

					SerConClientThread scct1 = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct1);

					scct1.start();
					scct1.notifyOther(u.getUserId());
				} else if (u.getUserId().equals("3")&& u.getPasswd().equals("12345")) {
					m.setMesType("1");
					oos.writeObject(m);

					SerConClientThread scct2 = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct2);

					scct2.start();
					scct2.notifyOther(u.getUserId());
				} else if (u.getUserId().equals("3")&& u.getPasswd().equals("12345")) {
					m.setMesType("1");
					oos.writeObject(m);

					SerConClientThread scct3 = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct3);

					scct3.start();
					scct3.notifyOther(u.getUserId());
				} else if (u.getUserId().equals("4")&& u.getPasswd().equals("12345")) {
					m.setMesType("1");
					oos.writeObject(m);

					SerConClientThread scct4 = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct4);

					scct4.start();
					scct4.notifyOther(u.getUserId());
				} else if (u.getUserId().equals("5")&& u.getPasswd().equals("12345")) {
					m.setMesType("1");
					oos.writeObject(m);

					SerConClientThread scct5 = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct5);

					scct5.start();
					scct5.notifyOther(u.getUserId());
				}else if (u.getUserId().equals("6")&& u.getPasswd().equals("12345")) {
					m.setMesType("1");
					oos.writeObject(m);

					SerConClientThread scct6 = new SerConClientThread(cs);

					ManageClientThread.addClientThread(u.getUserId(), scct6);

					scct6.start();
					scct6.notifyOther(u.getUserId());
				}else {
					m.setMesType("2");
					oos.writeObject(m);
					// close connect
					cs.close();
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	

}
