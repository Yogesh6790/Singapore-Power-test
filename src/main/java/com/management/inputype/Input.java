package com.management.inputype;

import java.util.List;

/**
 * 
 * This file is used for parsing PUT request's input parameters
 * @author Yogesh
 *
 */
public class Input {
	
	private List<String> friends;
	
	private String email;
	
	private String requestor;
	
	private String target;
	
	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
