package com.management.service;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.dao.FriendsMgmtDao;

/**
 * This is the Service which handles the Business Logic
 * @author Yogesh
 *
 */
@Service
public class FriendsMgmtServiceImpl implements FriendsMgmtService {
	
	@Autowired
	private FriendsMgmtDao friendsMgmtDao;

	/**
	 * 	@param emails (Having user1Email and user2Email)
	 *  @throws Exception 
	 */
	@Override
	public void makeFriends(List<String> emails) throws Exception{
		friendsMgmtDao.makeFriends(emails.get(0), emails.get(1));
	}

	/**
	 * 	@param email(user email address)
	 *  @return List<String> (Email Address of user's friends)
	 *  @throws Exception 
	 *  
	 */
	@Override
	public  List<String> retrieveFriends(String email) throws Exception{
		return friendsMgmtDao.retrieveFriends(email);
	}

	/**
	 * 	@param user1Email(user1 email address)
	 *	@param user2Email (user2 email address)
	 *  @return List<String> (Email Address of common friends between user1 and user2)
	 *  @throws Exception 
	 */
	@Override
	public  List<String> retrieveCommonFriends(String user1Email, String user2Email)  throws Exception{
		return friendsMgmtDao.retrieveCommonFriends(user1Email, user2Email);
	}

	/**
	 * 	@param requestor(requestor email address)
	 *	@param target (target email address)
	 *  @throws Exception 
	 */
	@Override
	public  void subscribeUpdates(String requestor, String target) throws Exception{
		friendsMgmtDao.subscribeUpdates(requestor, target);
	}

	/**
	 * 	@param requestor(requestor email address)
	 *	@param target (target email address)
	 *  @throws Exception 
	 */
	@Override
	public  void blockUpdates(String requestor, String target) throws Exception {
		 friendsMgmtDao.blockUpdates(requestor, target);
	}
	
	/**
	 * 	@param sender(sender email address)
	 * 	@param text(user's text note)
	 *  @return List<String> (Email Address of eligible people who can receive updates from the user)
	 *  @throws Exception
	 *   
	 */
	@Override
	public  List<String> retrieveEmailAddress(String sender, String text) throws Exception{
		
		List<String> eligibleAddress = friendsMgmtDao.retrieveEmailAddress(sender);
		if(text != null){
			int index = text.lastIndexOf(" ");
			if(index > -1){
				text = EmailValidator.getInstance()
				.isValid(text.substring(index+1)) ? text.substring(index+1) : "";
				if(!text.equals("") && !eligibleAddress.contains(text)){
					eligibleAddress.add(text);
				}
			}
		}
		return eligibleAddress;
	}

}
