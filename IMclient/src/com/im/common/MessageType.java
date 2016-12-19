/*
 * define the type of message package
 */
package com.im.common;

public interface MessageType {
		
	String message_succeed="1"; // login succeed
	String message_login_fail="2"; // login fail
	String message_comm_mes="3"; // common message
	String message_get_onLineFriend="4";  //request the online friends
	String message_ret_onLineFriend="5";  //return online friends
	
}
