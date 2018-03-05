package com.management.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.management.constants.FriendsMgmtConstants;
import com.management.entity.Relationship;
import com.management.util.Configuration;

/**
 * 
 * This is the Repository which provides interaction with the DB
 * @author Yogesh
 *
 */
@Repository
public class FriendsMgmtDaoImpl implements FriendsMgmtDao {
	
    @Autowired
    private Configuration configuration;

	@Override
	public void makeFriends(String user1Email, String user2Email)
			throws Exception {
		Session session = configuration.getHibernateFactory().openSession();
		session.beginTransaction();
		Relationship relationship1 = getRelationship(user1Email, user2Email, session);
		Relationship relationship2 = getRelationship(user2Email, user1Email, session);
		if (relationship1 != null && relationship2 != null
				&& relationship1.getFriend() == FriendsMgmtConstants.TRUE) {
			throw new Exception(FriendsMgmtConstants.ALREADY_FRIENDS);
		} else if (relationship1 != null && relationship1.getBlocked() == FriendsMgmtConstants.TRUE) {
			throw new Exception(relationship1.getUser1EmailAddress() + FriendsMgmtConstants.ALREADY_BLOCKED_1 + relationship1.getUser2EmailAddress());
		} else if(relationship2 != null && relationship2.getBlocked() == FriendsMgmtConstants.TRUE){
			throw new Exception(relationship2.getUser1EmailAddress() + FriendsMgmtConstants.ALREADY_BLOCKED_1 + relationship2.getUser2EmailAddress());
		}
		relationship1 = new Relationship(user1Email, user2Email);
		relationship2 = new Relationship(user2Email, user1Email);
		session.saveOrUpdate(relationship1);
		session.saveOrUpdate(relationship2);
		session.getTransaction().commit();
		session.close();
		System.out.println("Data Persisted to DB");
	}

	@Override
	public List<String> retrieveFriends(String userEmail) throws Exception{
		List<String> friendEmailAddresses = new ArrayList<String>();
		Session session = configuration.getHibernateFactory().openSession();
		session.beginTransaction();
		List<Relationship> relationshipList = getRelations(FriendsMgmtConstants.USER_1_EMAIL_ADDRESS_ATTRIBUTE,userEmail,session);
		session.getTransaction().commit();
		session.close();
		relationshipList.stream().forEach(x -> {
			if(!friendEmailAddresses.contains(x.getUser2EmailAddress())){
				friendEmailAddresses.add(x.getUser2EmailAddress());
			}
		});
		return friendEmailAddresses;
	}

	@Override
	public List<String> retrieveCommonFriends(String user1Email,
			String user2Email) throws Exception{
		List<String> commonEmailAddress = new ArrayList<String>();
		Session session = configuration.getHibernateFactory().openSession();
		session.beginTransaction();
		List<String> relationshipList1 = getRelations(FriendsMgmtConstants.USER_1_EMAIL_ADDRESS_ATTRIBUTE,user1Email,session).stream().map(x->x.getUser2EmailAddress()).collect(Collectors.toList());
		List<String> relationshipList2 = getRelations(FriendsMgmtConstants.USER_1_EMAIL_ADDRESS_ATTRIBUTE,user2Email,session).stream().map(x->x.getUser2EmailAddress()).collect(Collectors.toList());
		session.getTransaction().commit();
		session.close();
		if(relationshipList1.size()>relationshipList2.size()){
			commonEmailAddress = relationshipList1.stream().filter(x->relationshipList2.contains(x)).collect(Collectors.toList());
		}else{
			commonEmailAddress = relationshipList2.stream().filter(x->relationshipList1.contains(x)).collect(Collectors.toList());
		}
		commonEmailAddress.remove(user1Email);
		commonEmailAddress.remove(user2Email);
		return commonEmailAddress;
	}

	@Override
	public void subscribeUpdates(String requestor, String target)
			throws Exception {
		Session session = configuration.getHibernateFactory().openSession();
		session.beginTransaction();
		Relationship relationship = getRelationship(requestor, target, session);
		if (relationship != null) {
			if (relationship.getFollowing() == FriendsMgmtConstants.TRUE) {
				throw new Exception(FriendsMgmtConstants.ALREADY_SUBSCRIBED); // requestor has already subscribed to target 
			}
			relationship.setFollowing(FriendsMgmtConstants.TRUE);
			relationship.setBlocked(FriendsMgmtConstants.FALSE);
			session.saveOrUpdate(relationship);
		} else {
			Relationship following = new Relationship(requestor, target);
			following.setFriend(FriendsMgmtConstants.FALSE); // This case only requestor is following target
			session.save(following);
		}
		session.getTransaction().commit();
		session.close();
		System.out.println("Subscribed the person successfully");

	}

	@Override
	public void blockUpdates(String requestor, String target)
			throws Exception {
		Session session = configuration.getHibernateFactory().openSession();
		session.beginTransaction();
		Relationship relationship = getRelationship(requestor,target,session);
		if (relationship != null) {
			if (relationship.getFollowing() == FriendsMgmtConstants.FALSE || relationship.getBlocked() == FriendsMgmtConstants.TRUE) {
				throw new Exception(FriendsMgmtConstants.ALREADY_BLOCKED); // requestor has already subscribed to target 
			}
			relationship.setFollowing(FriendsMgmtConstants.FALSE);
			if(relationship.getFriend() == FriendsMgmtConstants.FALSE){
				relationship.setBlocked(FriendsMgmtConstants.TRUE);
			}
			session.saveOrUpdate(relationship);
		} else {
			Relationship blocked = new Relationship(requestor, target);
			blocked.setFriend(FriendsMgmtConstants.FALSE);
			blocked.setFollowing(FriendsMgmtConstants.FALSE);
			blocked.setBlocked(FriendsMgmtConstants.TRUE);
			session.save(blocked);
		}
		session.getTransaction().commit();
		session.close();
		System.out.println("Blocked the person successfully");
	}
	
	@Override
	public List<String> retrieveEmailAddress(String sender) throws Exception {
		List<String> eligibleAddress = new ArrayList<String>();
		Session session = configuration.getHibernateFactory().openSession();
		session.beginTransaction();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Relationship> query = session.getCriteriaBuilder()
				.createQuery(Relationship.class);
		Root<Relationship> root = query.from(Relationship.class);
		query.select(root)
				.where(builder.and(
						builder.equal(
								root.get(FriendsMgmtConstants.USER_2_EMAIL_ADDRESS_ATTRIBUTE),
								sender), builder.equal(
								root.get(FriendsMgmtConstants.BLOCKED),
								FriendsMgmtConstants.FALSE), builder.or(builder
								.equal(root.get(FriendsMgmtConstants.FRIEND),
										FriendsMgmtConstants.TRUE),
								builder.equal(root
										.get(FriendsMgmtConstants.FOLLOWING),
										FriendsMgmtConstants.TRUE))));
		Query<Relationship> q = session.createQuery(query);
		List<Relationship> list = q.getResultList();
		session.getTransaction().commit();
		session.close();
		list.stream().forEach(x -> {
			if(!eligibleAddress.contains(x.getUser1EmailAddress())){
				eligibleAddress.add(x.getUser1EmailAddress());
			}
		});
		return eligibleAddress;
	}
	
	private List<Relationship> getRelations(String user1EmailAddressAttribute,
			String userEmail, Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Relationship> query = session.getCriteriaBuilder()
				.createQuery(Relationship.class);
		Root<Relationship> root = query.from(Relationship.class);
		query.select(root).where(
				builder.and(builder.equal(root.get(user1EmailAddressAttribute),
						userEmail), builder.equal(
						root.get(FriendsMgmtConstants.FRIEND),
						FriendsMgmtConstants.TRUE)));
		Query<Relationship> q = session.createQuery(query);
		return q.getResultList();
	}
	
	private Relationship getRelationship(String requestor, String target, Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Relationship> query = session.getCriteriaBuilder()
				.createQuery(Relationship.class);
		Root<Relationship> root = query.from(Relationship.class);
		query.select(root)
				.where(builder.and(
						builder.equal(
								root.get(FriendsMgmtConstants.USER_1_EMAIL_ADDRESS_ATTRIBUTE),
								requestor),
						builder.equal(
								root.get(FriendsMgmtConstants.USER_2_EMAIL_ADDRESS_ATTRIBUTE),
								target)));
		// .having(builder.equal(root.get(FriendsMgmtConstants.USER_2_EMAIL_ADDRESS_ATTRIBUTE),
		// blockedPerson));
		Query<Relationship> q = session.createQuery(query);
		List<Relationship> list = q.getResultList();
		return list.size()>0 ? list.get(0) :null;
	}
	


}
