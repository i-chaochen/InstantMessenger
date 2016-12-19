/*
 * the thread is to manage client and server to keep connection 
 */
package com.im.tools;
import java.util.*;

public class ManageClientConServerThread {
		
	private static HashMap hm=new HashMap<String, ClientConServerThread>();
	
	//add ClientConServerThread in hashmap
	public static void addClientConServerThread(String imId, ClientConServerThread ccst) 
	{
		hm.put(imId, ccst);
	}
	
	//get this thread via imId
	public static ClientConServerThread getClientConServerThread(String imId)
	{
		return (ClientConServerThread)hm.get(imId);
	}
}
