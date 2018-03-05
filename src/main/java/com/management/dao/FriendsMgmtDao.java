package com.management.dao;

import java.util.List;
import java.util.Map;

public interface FriendsMgmtDao {

	
	public void makeFriends(String user1Email, String user2Email) throws Exception;
	
	public List<String> retrieveFriends(String userEmail) throws Exception;
	
	public List<String> retrieveCommonFriends(String user1Email, String user2Email) throws Exception;
	
	public void subscribeUpdates(String requestor, String target) throws Exception;
	
	public void blockUpdates(String requestor, String target) throws Exception;
	
	public List<String> retrieveEmailAddress(String sender) throws Exception;
}
