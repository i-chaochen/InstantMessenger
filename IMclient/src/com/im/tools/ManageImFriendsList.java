/*
 * manage friends, black list window class
 */
package com.im.tools;
import java.util.*;
import com.im.client.view.*;
import java.io.*;

public class ManageImFriendsList {
	
	private static HashMap hm=new HashMap<String, ImFriendsList>();
	
	public static void addImFriendsList(String imid,ImFriendsList imFriendsList)
	{
		hm.put(imid, imFriendsList);
	}
	
	public static ImFriendsList getImFriendsList(String imId)
	{
		return (ImFriendsList)hm.get(imId);
	}
}
