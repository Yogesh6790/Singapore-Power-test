package com.management.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.management.constants.FriendsMgmtConstants;
import com.management.inputype.Input;
import com.management.service.FriendsMgmtService;

/**
 * This is the RestController which handles the incoming API Requests
 * @author Yogesh
 *
 */
@RestController
public class FriendsMgmtController {

	@Autowired
	private FriendsMgmtService friendsMgmtService;

	/**
	 * 
	 * This method is used to make two persons as friends.
	 * HTTP PUT Method is used here because this operation is involved in 
	 * 	  1. Creating records for two persons, if the records didn't exist, individually in DB.
	 * 	  2. Updating records of two persons, if the records already exist, in DB.
	 * IDEMPOTENCY is the major reason why PUT is chosen over POST.
	 * 
	 * @param entity
	 * @return JSON
	 */
	@RequestMapping(value = FriendsMgmtConstants.MAKE_FRIENDS, method = RequestMethod.PUT,
			consumes = FriendsMgmtConstants.JSON, produces = FriendsMgmtConstants.JSON)
	public @ResponseBody String makeFriends(HttpEntity<String> entity) {
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(FriendsMgmtConstants.SUCCESS, false);
		try {
			Input input = getInput(entity.getBody()); // get Input
			List<String> emails = input.getFriends();
			if (emails.size() == 2 && !emails.get(0).equals(emails.get(1))
					&& EmailValidator.getInstance().isValid(emails.get(0))
					&& EmailValidator.getInstance().isValid(emails.get(1))) {
				friendsMgmtService.makeFriends(emails);
				resultMap.put(FriendsMgmtConstants.SUCCESS, true);
			} else {
				resultMap.put(FriendsMgmtConstants.MESSAGE,
						FriendsMgmtConstants.EMAIL_NOT_VALID);
			}
		} catch (Exception e) {
			// handling exception in order to provide meaningful message as JSON
			// response
			System.err.println(e.getMessage());
			String errorMsg = "";
			if(e.getMessage().equals(FriendsMgmtConstants.ALREADY_FRIENDS)){
				errorMsg = FriendsMgmtConstants.ALREADY_FRIENDS;
			}else if (e.getMessage().contains(FriendsMgmtConstants.ALREADY_BLOCKED_1)){
				errorMsg = e.getMessage();
			}
			resultMap.put(FriendsMgmtConstants.MESSAGE,
					FriendsMgmtConstants.ERROR_IN_MAKING_FRIENDS+errorMsg);
		}
		return getResult(resultMap);
	}

	/**
	 * This method is used to retrieve friends of a person.
	 * HTTP GET method is used here because this operation is 
	 * involved in just retrieving friends of a person from DB.
	 * 
	 * @param entity
	 * @return JSON
	 */
	@RequestMapping(value = FriendsMgmtConstants.RETRIEVE_FRIENDS, method = RequestMethod.GET,
			consumes = FriendsMgmtConstants.JSON, produces = FriendsMgmtConstants.JSON)
	public @ResponseBody String retrieveFriends(HttpEntity<String> entity) {
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(FriendsMgmtConstants.SUCCESS, false);
		try {
			List<String> emails = entity.getHeaders().get(FriendsMgmtConstants.EMAIL);
			if (emails != null && emails.size() > 0
					&& EmailValidator.getInstance().isValid(emails.get(0))) {
				List<String> friendsList = friendsMgmtService
						.retrieveFriends(emails.get(0));
				resultMap.put(FriendsMgmtConstants.SUCCESS, true);
				if (friendsList.size() == 0) {
					resultMap.put(FriendsMgmtConstants.MESSAGE,
							FriendsMgmtConstants.NO_FRIENDS);
				} else {
					resultMap.put(FriendsMgmtConstants.FRIENDS, friendsList);
					resultMap.put(FriendsMgmtConstants.COUNT,
							friendsList.size());
				}
			} else {
				resultMap.put(FriendsMgmtConstants.MESSAGE,
						FriendsMgmtConstants.EMAIL_NOT_VALID_1);
			}
		} catch (Exception e) {
			// handling exception in order to provide meaningful message as JSON
			// response
			System.err.println(e.getMessage());
			resultMap.put(FriendsMgmtConstants.MESSAGE,
					FriendsMgmtConstants.ERROR_IN_RETRIEVING_FRIENDS);
		}
		return getResult(resultMap);
	}
	

	/**
	 * This method is used to retrieve common friends of two persons.
	 * HTTP GET method is used here because this operation is 
	 * involved in just retrieving friends of two persons from DB.
	 * And then then filtering is further done to find out the common friends.
	 * 
	 * @param entity
	 * @return JSON
	 */
	@RequestMapping(value = FriendsMgmtConstants.RETRIEVE_COMMON_FRIENDS, method = RequestMethod.GET,
			consumes = FriendsMgmtConstants.JSON, produces = FriendsMgmtConstants.JSON)
	public @ResponseBody String retrieveCommonFriends(HttpEntity<String> entity) {
		Map<String, Object> resultMap = new LinkedHashMap<>();
 		resultMap.put(FriendsMgmtConstants.SUCCESS, false);
 		try{
 			List<String> email1 = entity.getHeaders().get(FriendsMgmtConstants.EMAIL_1);
 			List<String> email2 = entity.getHeaders().get(FriendsMgmtConstants.EMAIL_2);
			if (email1 != null && email1.size() > 0 && email2 != null
					&& email2.size() > 0
					&& !email1.get(0).equals(email2.get(0))
					&& EmailValidator.getInstance().isValid(email1.get(0))
					&& EmailValidator.getInstance().isValid(email2.get(0))) {
				List<String> commonFriends = friendsMgmtService
						.retrieveCommonFriends(email1.get(0), email2.get(0));
				resultMap.put(FriendsMgmtConstants.SUCCESS, true);
				if (commonFriends.size() == 0) {
					resultMap.put(FriendsMgmtConstants.MESSAGE,
							FriendsMgmtConstants.NO_COMMON_FRIENDS);
				} else {
					resultMap.put(FriendsMgmtConstants.FRIENDS, commonFriends);
					resultMap.put(FriendsMgmtConstants.COUNT,
							commonFriends.size());
				}
			} else{
 				resultMap.put(FriendsMgmtConstants.MESSAGE, FriendsMgmtConstants.EMAIL_NOT_VALID);
 			}
 		}catch(Exception e){
			// handling exception in order to provide meaningful message as JSON
			// response
			System.err.println(e.getMessage());
			resultMap.put(FriendsMgmtConstants.MESSAGE,
					FriendsMgmtConstants.ERROR_IN_RETRIEVING_COMMON_FRIENDS);
 		}
 		return getResult(resultMap);
	}
	
	/**
	 * This method is used to make one person to subscribe to updates to another person.
	 * HTTP PUT Method is used here because this operation is involved in 
	 * 	  1. Creating records for two persons, if the records didn't exist, individually in DB.
	 * 	  2. Updating records of two persons, if the records already exist, in DB.
	 * IDEMPOTENCY is the major reason why PUT is chosen over POST.
	 * 
	 * @param entity
	 * @return JSON
	 */
	@RequestMapping(value = FriendsMgmtConstants.SUBSCRIBE_UPDATES, method = RequestMethod.PUT,
			consumes = FriendsMgmtConstants.JSON, produces = FriendsMgmtConstants.JSON)
	public @ResponseBody String subscribeUpdates(HttpEntity<String> entity) {
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(FriendsMgmtConstants.SUCCESS, false);
		try {
			Input input  = getInput(entity.getBody()); // get Input
			String requestor = input.getRequestor();
			String target = input.getTarget();
			if (!requestor.equals(target) && EmailValidator.getInstance().isValid(requestor)
					&& EmailValidator.getInstance().isValid(target)) {
				friendsMgmtService.subscribeUpdates(requestor, target);
				resultMap.put(FriendsMgmtConstants.SUCCESS, true);
			} else {
				resultMap.put(FriendsMgmtConstants.MESSAGE,
						FriendsMgmtConstants.EMAIL_NOT_VALID);
			}
		} catch (Exception e) {
			// handling exception in order to provide meaningful message as JSON
			// response
			System.err.println(e.getMessage());
			String errorMsg = e.getMessage().equals(FriendsMgmtConstants.ALREADY_SUBSCRIBED) ? FriendsMgmtConstants.ALREADY_SUBSCRIBED : "";
			resultMap.put(FriendsMgmtConstants.MESSAGE,
					FriendsMgmtConstants.ERROR_IN_SUBSCRIBING + errorMsg );
		}
		return getResult(resultMap);
	}
	
	/**
	 * This method is used to make one person to block updates of another person.
	 * HTTP PUT Method is used here because this operation is involved in 
	 * 	  1. Creating records for two persons, if the records didn't exist, individually in DB.
	 * 	  2. Updating records of two persons, if the records already exist, in DB.
	 * IDEMPOTENCY is the major reason why PUT is chosen over POST.
	 * 
	 * @param entity
	 * @return JSON
	 */
	@RequestMapping(value = FriendsMgmtConstants.BLOCK_UPDATES, method = RequestMethod.PUT,
			consumes = FriendsMgmtConstants.JSON, produces = FriendsMgmtConstants.JSON)
	public @ResponseBody String blockUpdates(HttpEntity<String> entity) {
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(FriendsMgmtConstants.SUCCESS, false);
		try {
			Input input  = getInput(entity.getBody()); // get Input
			String requestor = input.getRequestor();
			String target = input.getTarget();
			if (!requestor.equals(target) && EmailValidator.getInstance().isValid(requestor)
					&& EmailValidator.getInstance().isValid(target)) {
				friendsMgmtService.blockUpdates(requestor, target);
				resultMap.put(FriendsMgmtConstants.SUCCESS, true);
			} else {
				resultMap.put(FriendsMgmtConstants.MESSAGE,
						FriendsMgmtConstants.EMAIL_NOT_VALID);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			String errorMsg = e.getMessage().equals(FriendsMgmtConstants.ALREADY_BLOCKED) ? FriendsMgmtConstants.ALREADY_BLOCKED : "";
			resultMap.put(FriendsMgmtConstants.MESSAGE,
					FriendsMgmtConstants.ERROR_IN_BLOCKING_A_USER + errorMsg);
		}
		return getResult(resultMap);
	}
	
	/**
	 * This method is used to retrieve all email addresses from which a person can receive updates from.
	 * HTTP GET method is used here because this operation is 
	 * involved in just retrieving friends and followers of a person from DB.
	 * 
	 * @param entity
	 * @return JSON
	 */
	@RequestMapping(value = FriendsMgmtConstants.RETRIEVE_EMAIL_ADDRESS, method = RequestMethod.GET,
			consumes = FriendsMgmtConstants.JSON, produces = FriendsMgmtConstants.JSON)
	public @ResponseBody String retrieveEmailAddress(HttpEntity<String> entity){
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(FriendsMgmtConstants.SUCCESS, false);
		try {
			List<String> sender = entity.getHeaders().get(FriendsMgmtConstants.SENDER);
			String text = entity.getHeaders().get(FriendsMgmtConstants.TEXT) != null
					&& entity.getHeaders().get(FriendsMgmtConstants.TEXT)
							.size() > 0 ? entity.getHeaders()
					.get(FriendsMgmtConstants.TEXT).get(0) : null;
			if (sender != null && sender.size()>0 && EmailValidator.getInstance().isValid(sender.get(0))) { // check if email are valid
 				List<String> eligibleUsers = friendsMgmtService.retrieveEmailAddress(sender.get(0),text);
				resultMap.put(FriendsMgmtConstants.SUCCESS, true);
				if(eligibleUsers.size() == 0){
					resultMap.put(FriendsMgmtConstants.MESSAGE, FriendsMgmtConstants.NO_ELIGIBLE_USERS+sender.get(0));
				}else{
					resultMap.put(FriendsMgmtConstants.RECIPIENTS, eligibleUsers);
				}
			} else {
				resultMap.put(FriendsMgmtConstants.MESSAGE, FriendsMgmtConstants.EMAIL_NOT_VALID_1);
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			resultMap.put(FriendsMgmtConstants.MESSAGE, FriendsMgmtConstants.ERROR_IN_GETTING_ELIGIBLE_EMAILS);
		}
		return getResult(resultMap);
	}
	
	private Input getInput(String json) {
		return new Gson().fromJson(json, Input.class);
	}

	private String getResult(Map<String, Object> resultMap) {
		return new Gson().toJson(resultMap);
	}
	
}
