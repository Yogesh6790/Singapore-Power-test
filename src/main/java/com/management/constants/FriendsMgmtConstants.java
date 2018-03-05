package com.management.constants;

/**
 * This file contains all the constant variables
 * @author Yogesh
 *
 */
public class FriendsMgmtConstants {
	
	//API methods 
	
	public static final String MAKE_FRIENDS = "makeFriends";
	
	public static final String RETRIEVE_FRIENDS = "retrieveFriends";
	
	public static final String RETRIEVE_COMMON_FRIENDS = "retrieveCommonFriends";
	
	public static final String SUBSCRIBE_UPDATES = "subscribeUpdates";
	
	public static final String BLOCK_UPDATES = "blockUpdates";
	
	public static final String RETRIEVE_EMAIL_ADDRESS = "retrieveEmailAddress";
	
	public static final String JSON = "application/json";
	
	// Execution Status	
	public static final int EXECUTION_SUCCESS = 1;
	
	public static final int EXECUTION_FAILURE = 0;
	
	//JSON messages	
	public static final String EMAIL_NOT_VALID = "Emails are not valid. Please check! ";
	
	public static final String EMAIL_NOT_VALID_1 = "Email is not valid. Please check! ";
	
	public static final String ERROR_IN_MAKING_FRIENDS = "Error in making both of them as friends. ";
	
	public static final String ALREADY_FRIENDS = "They are already friends";
	
	public static final String ALREADY_BLOCKED_1 = " has already blocked ";
	
	public static final String ERROR_IN_RETRIEVING_FRIENDS = "Error in retrieving friends";
	
	public static final String NO_FRIENDS = "No friends found.";
	
	public static final String ERROR_IN_RETRIEVING_COMMON_FRIENDS = "Error in retrieving common friends";
	
	public static final String NO_COMMON_FRIENDS = "No common friends found.";
	
	public static final String ERROR_IN_SUBSCRIBING = "Error occured in subscribing. ";
	
	public static final String ALREADY_SUBSCRIBED = "Requestor has already subscribed";
	
	public static final String ALREADY_BLOCKED = "Requestor has already blocked";
	
	public static final String ERROR_IN_BLOCKING_A_USER = "Error in blocking a user. ";
	
	public static final String ERROR_IN_GETTING_ELIGIBLE_EMAILS = "Error in finding eligible people for receiving updates";
	
	public static final String NO_ELIGIBLE_USERS = "No users are eligible to receive the update from ";

	public static final String SUCCESS = "success";
	
	public static final String MESSAGE = "message";
	
	public static final String FRIENDS = "friends";
	
	public static final String RECIPIENTS = "recipients";
	
	public static final String COUNT = "count";
	
	//VARIABLES
	public static final String EMAIL = "email";
	
	public static final String EMAIL_1 = "email_1";
	
	public static final String EMAIL_2 = "email_2";
	
	public static final String SENDER = "sender";
	
	public static final String TEXT = "text";
	
	//QUERY VARIABLES
	public static final String EMAIL_ADDRESS = "email_address";
	
	public static final String USER_1_EMAIL_ADDRESS_ATTRIBUTE = "user1EmailAddress";
	
	public static final String USER_2_EMAIL_ADDRESS_ATTRIBUTE = "user2EmailAddress";
	
	public static final String FRIEND = "friend";
	
	public static final String FOLLOWING = "following";
	
	public static final String BLOCKED = "blocked";
	
	public static final int TRUE = 1;
	
	public static final int FALSE = 0;
	
	public static final String SELECT_QUERY = "from person where email_address = ";
	
	
	
	
}
