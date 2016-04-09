package com.rex.backend.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rex.backend.entity.Job;
import com.rex.backend.entity.User;

public class UserService {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public UserService(){
		emf = Persistence.createEntityManagerFactory("reportengine");
        em = emf.createEntityManager();
	}
	
	public User findUser(String id) {
		return em.find(User.class, id);
	}
	
}
