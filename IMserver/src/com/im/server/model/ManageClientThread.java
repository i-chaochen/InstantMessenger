 /**
 * 1. keep client connecting to server's thread
 * 2. keep the sync server's socket thread
 * 3. keep the sync server'name and object
 * 4. keep the local users
 * 5. keep the sync users 
 */
package com.im.server.model;
import java.io.IOException;
import java.util.*;

import javax.swing.DefaultListModel;

import com.im.common.Command;
import com.im.common.Message;
import com.im.common.User;
import com.im.common.Utils;

public class ManageClientThread {
	
	/**
	 * keep all clients connecting to servers' thread
	 * KEY is the user id
	 */
	private static Map<String, SerConClientThread> cliConnSerMap = 
		Collections.synchronizedMap(new HashMap<String, SerConClientThread>());
	
	/**
	 * keep sync servers' connected thread
	 * KEY is the serverName, such as localhost1, localhot2
	 */
	private static Map<String, SerConClientThread> syncConnMap = 
		Collections.synchronizedMap(new HashMap<String, SerConClientThread>());
	
	/**
	 * keep sync server's name and object
	 * KEY is the ServerName, such as localhost1, localhost2
	 * VALUE is the server object
	 * @see com.im.common.User
	 */
	private static Map<String, User> syncServerMap = 
		Collections.synchronizedMap(new HashMap<String, User>());
	
	/**
	 * this MAP is for keep local users 
	 * KEY is user id
	 * VALUE is user object
	 * @see com.im.common.User
	 */
	private static Map<String, User> localUserMap = 
		Collections.synchronizedMap(new HashMap<String, User>());
	
	/**
	 * the MAP is for keeping other sync server's user
	 * KEY is user id
	 * VALUE is user object
	 * @see com.im.common.User
	 */
	private static Map<String, User> syncUserMap = 
		Collections.synchronizedMap(new HashMap<String, User>());
	
	/**
	 * the method of adding other sync servers' user
	 * @param user
	 */
	public static synchronized void addSyncUser(User user)
	{
		//if the syncUserMap already contains this user, not put
		if(syncUserMap.containsKey(user.getUserId()))
		{
			return;
		}
		//notify local user: user login
		notifyOnline(user.getUserId(), true);
		syncUserMap.put(user.getUserId(), user);
	}
	
	/**
	 * the method of removing users from syncUserMap, if user logout sync servers
	 * @param user
	 */
	public static synchronized void removeSyncUser(User user)
	{
		syncUserMap.remove(user.getUserId());
		//notify all online friends: user logout
		notifyOnline(user.getUserId(), false);
	}
	
	/**
	 * the method of getting users kept in sync servers 
	 * @return
	 */
	public static synchronized String getSyncUsers()
	{
		String users = "";
		for(Iterator<User> iterator = syncUserMap.values().iterator();iterator.hasNext();)
		{
			users += iterator.next().toString() + "\n";
		}
		return Utils.isNullString(users) ? "< NONE OF USER LOGIN OTHER SERVER >" : users;
	}
	
	/**
	 * iterate sync servers' user
	 * put user in an ArrayList
	 * @param serverName
	 * @return
	 */
	public static Iterator<User> iteratorSyncUser(String serverName)
	{	
		User user = null;
		List<User> list = new ArrayList<User>();
		for(Iterator<User> iter = syncUserMap.values().iterator(); iter.hasNext();)
		{
	 		user = iter.next();
			if(serverName.equals(user.getServerName()))
					{
				list.add(user);
					}
		}
		return list.iterator();
	}
	
	/**
	 * iterate local's user thread
	 * @return
	 */
	public static synchronized Iterator<SerConClientThread> iteratorConClient()
	{
		return cliConnSerMap.values().iterator();
	}
	
	/**
	 * iterate localuserMap's user
	 * @return
	 */
	public static synchronized Iterator<User>  iteratorUserId()
	{
		return localUserMap.values().iterator();
	}
	
	/**
	 * the method of adding connected thread between local and sync servers in cache
	 * @param user
	 * @param ct
	 * @return
	 */
	public static synchronized boolean addSyncThread(User user, SerConClientThread ct)
	{
		//if servers already have users, not add and return false
		if(syncServerMap.containsKey(user.getUserId()))
			{
			return false;
			}
		syncServerMap.put(user.getUserId(), user);
		syncConnMap.put(user.getUserId(),ct);	
	
		//transmit the localUserMap to other sync servers
		for(Iterator<User> iter = localUserMap.values().iterator(); iter.hasNext();)
		{	//the sync user message and command
			Message m = new Message();
			m.setCommand(Command.SYNC_USER);
			m.setCon(iter.next());
			
			try {
				ct.getMessageTransmitter().transmitToClient(m);
			} catch (IOException e) {
			}
		}
		return true;
	}
	
	/**
	 * the method of removing the sync server connected thread 
	 * @param user
	 */
	public static synchronized void removeSyncThread(User user)
	{
		String userId = user.getUserId();
		syncServerMap.remove(userId);
		SerConClientThread st = syncConnMap.remove(userId);
		
		for(Iterator<User> iter = iteratorSyncUser(userId); iter.hasNext();)
		{
			syncUserMap.remove(iter.next().getUserId());
		}

		if(st != null)
		{
			st.close();
			st = null;
		}
	}
	
	/**
	 * add a connected thread into cache
	 */
	public static synchronized boolean addClientThread(User user, SerConClientThread ct)
	{
		String uid = user.getUserId();
		
		//if local and sync servers already have users, not add and return false
		if(localUserMap.containsKey(uid) ||syncUserMap.containsKey(uid))
			{
			return false;
			}
		
		//notify local users: user login
		notifyOnline(uid, true);
		
		//put this cliConnSerMap and localUserMap in the cache
		cliConnSerMap.put(uid,ct);
		localUserMap.put(uid, user);
		
		return true;
	}
	
	/**
	 * the method of removing the client's connected thread from local server, if client is logout
	 * @param user
	 */
	public static synchronized void removeClientThread(User user)
	{
		String userId = user.getUserId();
		SerConClientThread sc = cliConnSerMap.remove(userId);
		if(sc != null)
		{	
			//close the socket connection
			sc.close();
			sc = null;
			
			//remove users from cache
			localUserMap.remove(userId);
			
			//notify local users that user is login
			notifyOnline(userId, false);
			
			//notify other sync servers that the local user is logout
			user.setIsLogin(false);
			syncUserWithServer(user);
		}
	}
	
	/**
	 * the method of clearing all cache, if the server is closed
	 */
	public static synchronized void clearClientThread()
	{
		//close this user's connection thread
		for(Iterator<SerConClientThread> iter = cliConnSerMap.values().iterator(); iter.hasNext();)
		{
			iter.next().close();
		}
		
		//close this sync server threads
		for(Iterator<SerConClientThread> iter = syncConnMap.values().iterator(); iter.hasNext();)
		{
			iter.next().close();
		}
		//clear these cache
		syncConnMap.clear();
		cliConnSerMap.clear();
		localUserMap.clear();
		syncUserMap.clear();
	}
	
	/**
	 * get the user's connected thread id
	 * @param uid
	 * @return
	 */
	public static SerConClientThread getClientThread(String uid)
	{
		return (SerConClientThread)cliConnSerMap.get(uid);
	}
	
	/**
	 * get the sync servers' connected thread
	 * @param uid
	 * @return
	 */
	public static SerConClientThread getSyncUserThread(String uid)
	{	
		//the sync online users
		User user = syncUserMap.get(uid);
		if(user == null)
		{
			return null;
		}
		
		String serverName = user.getServerName();
		
		return syncConnMap.get(serverName);	
	}
	
	/**
	 * return online friends login local and sync servers.
	 */
	public static synchronized String getAllOnLineUserid()
	{	
		//local server's user
		Iterator<String> it=localUserMap.keySet().iterator();
		String res=""; // result
		while(it.hasNext())
		{
			res+=it.next()+" ";
		}
		//other sync server's user
		it = syncUserMap.keySet().iterator();
		while(it.hasNext())
		{
			res+=it.next()+" ";
		}
		return res;
	}
	
	/**
	 * make this thread to notify other clients
	 */
	private static void notifyOnline(String loginUser, boolean isLogin)
	{
		String onlineUserId;
		User onlineUser;
		//notify local server's user
		for(Iterator<User> iter = iteratorUserId(); iter.hasNext();)
		{
			onlineUser = iter.next();
			onlineUserId = onlineUser.getUserId();
			
			Message message=new Message();
			message.setCon(loginUser);
			message.setGetter(onlineUserId);
			message.setCommand(isLogin ? Command.USER_LOGIN_LIST : Command.USER_LOGOUT_LIST);
			
			try {
				getClientThread(onlineUserId).getMessageTransmitter().transmitToClient(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * notify other sync server's user: login or logout
	 * @param user
	 */
	public static void syncUserWithServer(User user)
	{
		Message message=new Message();
		message.setCommand(Command.SYNC_USER);
		message.setCon(user);
		
		String isLogin = user.isLogin() ? "login" : "logout";
		//the sync servers' connected sockets thread map
		for(Iterator<SerConClientThread> iter = syncConnMap.values().iterator();iter.hasNext();)
		{
			try {
				iter.next().getMessageTransmitter().transmitToClient(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}	

