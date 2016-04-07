package com.rex.backend.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.GroupPK;
import com.rex.backend.entity.Job;
import com.rex.backend.entity.UserGroup;

/**
 * 
 * @author Ryan Hsu
 */

public class JobService {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public JobService(){
		emf = Persistence.createEntityManagerFactory("reportengine");
        em = emf.createEntityManager();
	}
	
	public Job createJob(String id, String name, String macro) {
		Job job = new Job(id, name, macro);
		em.persist(job);

		return job;
	}
	
	public Job addJob(Job job){
		em.getTransaction().begin();
		em.persist(job);
		em.getTransaction().commit();
		
		return job;
	}

	public void removeJob(String id) {
		Job job = em.find(Job.class, id);

		if (job != null) {
			em.remove(job);
		}
	}
	
	public Job findJob(String id) {
		//em.getTransaction().begin();
		//Job job = em.find(Job.class, id);
		//em.getTransaction().commit();
		
		return em.find(Job.class, id);
	}
	
	public void addFreqs(String id, List<Freq> freqs){
		Job job = findJob(id);
		
		try{
			em.getTransaction().begin();
			
			job.getFreqs().addAll(freqs);
			
			//job.setFreqs(freqs);
			em.persist(job);
			em.getTransaction().commit();
			
	    }finally{
	    	
	    }
		
	}
	
	public void addUserGroup(String jobid, List<String> userList, String tocc){
		for (String userid : userList) {
			UserGroup group = new UserGroup();
			
			group.setJobID(jobid);
			group.setUserID(userid);
			group.setTocc(tocc);

			try {
				em.getTransaction().begin();
				em.persist(group);
				em.getTransaction().commit();
			} catch (Exception e) {
	            em.getTransaction().rollback();
	            e.printStackTrace();
	        } finally {

			}
		}
	}
	
	public void removeUserGroup(String jobid, List<String> userList){
		
		for (String userid : userList) {
			GroupPK pk = new GroupPK();
			
			pk.setJobID(jobid);
			pk.setUserID(userid);

			try {
				UserGroup group = em.find(UserGroup.class, pk);

				if (group != null) {
					em.getTransaction().begin();
					em.remove(group);
					em.getTransaction().commit();
				}
				
			} catch (Exception e) {
	            em.getTransaction().rollback();
	            e.printStackTrace();
	        } finally {

			}
		}
		
	}
	
	public List<Job> findAllJob() {
		TypedQuery<Job> query = em.createQuery("SELECT * FROM Job", Job.class);
		return query.getResultList();
	}
	
	public List<UserGroup> findUsersByGroup(String jobid){
		TypedQuery<UserGroup> query = em.createQuery("SELECT g FROM USERGROUP g WHERE g.jobID =?1", UserGroup.class);
		query.setParameter(1, jobid);
		return query.getResultList();
	}
	
	public void removeFreqs(String id, List<Freq> freqs){
		Job job = findJob(id);
		
		try{
			em.getTransaction().begin();
			
			job.getFreqs().removeAll(freqs);
			
			em.persist(job);
			em.getTransaction().commit();
			
	    }finally{
	    }
	}
}
