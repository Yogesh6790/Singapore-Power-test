package com.management.service;

import java.util.List;
import java.util.Map;


public interface FriendsMgmtService {
	
	public void makeFriends(List<String> emails) throws Exception;
	
	public List<String> retrieveFriends(String email) throws Exception;

	public List<String> retrieveCommonFriends(String user1Email, String user2Email) throws Exception;
	
	public void subscribeUpdates(String requestor, String target) throws Exception;

	public void blockUpdates(String requestor, String target) throws Exception;

	public List<String> retrieveEmailAddress(String sender, String text) throws Exception;

}
