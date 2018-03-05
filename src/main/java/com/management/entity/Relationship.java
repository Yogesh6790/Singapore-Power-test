package com.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.management.constants.FriendsMgmtConstants;

/**
 * 
 * Assumption : User records already exist in database.
 * 
 * 
 * This is the only Entity that is persisted to the DB.
 * It is an optimal design approach as it improves the performance while 
 * retrieving/ updating records in DB.
 * 
 * @author Yogesh
 *
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "relationship")
public class Relationship {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
    private Integer id;
	
	@Column(name = "user1_email_address")
	@NotNull
	private String user1EmailAddress;
	
	@Column(name = "user2_email_address")
	@NotNull
	private String user2EmailAddress;
	
	@Column(name = "friend")
	private int friend;
	
	@Column(name = "following")
	private int following;
	
	@Column(name = "blocked")
	private int blocked;
	
	public Relationship(){
	}
	
	public Relationship(String emailAddress1, String emailAddress2){
		this.user1EmailAddress = emailAddress1;
		this.user2EmailAddress = emailAddress2;
		this.friend = FriendsMgmtConstants.TRUE;
		this.following = FriendsMgmtConstants.TRUE;
		this.blocked = FriendsMgmtConstants.FALSE;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser1EmailAddress() {
		return user1EmailAddress;
	}

	public void setUser1EmailAddress(String user1EmailAddress) {
		this.user1EmailAddress = user1EmailAddress;
	}

	public String getUser2EmailAddress() {
		return user2EmailAddress;
	}

	public void setUser2EmailAddress(String user2EmailAddress) {
		this.user2EmailAddress = user2EmailAddress;
	}

	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

	public int getBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}
	
}