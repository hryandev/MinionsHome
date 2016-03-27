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

public class FreqService {
	
	private EntityManager em;
	
	public FreqService(EntityManager em){
		this.em = em;
	}
	
	public Freq addFreq(Freq freq){
		em.getTransaction().begin();
		em.persist(freq);
		em.getTransaction().commit();
		
		return freq;
	}
	
	public void updateFreqStartTime(String freqid, String sTime){
		Freq freq = em.find(Freq.class, freqid);
		
		em.getTransaction().begin();
		freq.setStartTime(sTime);
		em.getTransaction().commit();
		
		return;
	}
	
	public void removeFreq(String id) {
		Freq freq = em.find(Freq.class, id);

		if (freq != null) {
			em.remove(freq);
		}
	}
	
	public Freq findFreq(String id) {
		return em.find(Freq.class, id);
	}
	
	public List<Freq> findAllFreq() {
		TypedQuery<Freq> query = em.createQuery("SELECT * FROM Freq", Freq.class);
		return query.getResultList();
	}
	
	public void addJobList(String id, List<Job> joblist){
		Freq freq = findFreq(id);
		
		try{
			em.getTransaction().begin();
			
			freq.getJobList().addAll(joblist);
			
			em.persist(freq);
			em.getTransaction().commit();
			
	    }finally{
	    	
	    }
		
	}
	
	public void removeJobList(String id, List<Job> joblist){
		Freq freq = findFreq(id);
		
		try{
			em.getTransaction().begin();
			
			freq.getJobList().removeAll(joblist);
			
			em.persist(freq);
			em.getTransaction().commit();
			
	    }finally{
	    }
	}
	
}
