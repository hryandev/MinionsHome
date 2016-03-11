package com.rex.backend.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@IdClass(JobFreqPK.class)
public class JobFreq {
	
	public JobFreq(){
		
	}
	
	@Id
    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "JOB_ID")
    private Job job;
	
	@Id
    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "FREQ_ID")
    private Freq freq;

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Freq getFreq() {
		return freq;
	}

	public void setFreq(Freq freq) {
		this.freq = freq;
	}
	
}
