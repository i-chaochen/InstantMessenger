/*
 * the interface for executing all commands
 */
package com.im.common;

public interface Command 

{
	  int LOGIN=0;  // LOGIN REQUREST;
	  int LOGIN_SUCCEED=1;  // LOGIN SUCCEED;
	  int LOGIN_FAIL=2;     //LOGIN FAIL;
	  int COMM_MES=3;   	//COMMON MESSAGE;
	  int GET_ONLINEFRIEND=4;  	//get the list of online friends;
	  int USER_LOGIN_LIST=5;	   //return the list of logout friends;
	  int LOGOUT=6;   		//logout command;
	  int SYNC_CONN=7;   	//THE SYNCHRONIZED COMMAND IS CONNECTING;
	  int SYNC_USER=8;   	//the synchronized servers' message;
	  int SYNC_OK = 9;    //the sync is succeed;
	  int USER_LOGOUT_LIST = 10;	//the list of logout friends
	  
	  //executing all these commands
	  public void execute(Message message) throws Exception;
	  
}	
