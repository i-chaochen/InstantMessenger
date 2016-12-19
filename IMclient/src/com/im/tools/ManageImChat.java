/*
 * it is for manage im chat
 */
package com.im.tools;
import java.util.*;
import com.im.client.view.*;
public class ManageImChat {
	private static HashMap hm=new HashMap<String,ImChat>();
	
	//add
	public static void addImChat(String loginIdAndFriendId, ImChat imChat)
	{
		hm.put(loginIdAndFriendId, imChat);
	}
	
	//get
	public static ImChat getImChat(String loginIdAndFriendId)
	{
		return (ImChat)hm.get(loginIdAndFriendId);
	}
}
