package com.rex.backend.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.rex.backend.entity.Freq;
import com.rex.backend.entity.Job;
import com.rex.backend.entity.JobFreq;

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
	
	public List<Job> findAllJob() {
		TypedQuery<Job> query = em.createQuery("SELECT * FROM Job", Job.class);
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
