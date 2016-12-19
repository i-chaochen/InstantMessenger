/*
 * check the user
 */
package com.im.client.model;

import com.im.common.*;

public class ImClientUser {
	
		public boolean checkUser(User u)
		{
			return new ImClientConServer().sendLoginInfoToServer(u);
		}
}



