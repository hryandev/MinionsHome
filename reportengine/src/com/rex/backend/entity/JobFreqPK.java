package com.rex.backend.entity;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class JobFreqPK implements Serializable{
	public JobFreqPK(){
		
	}
	
	//private Job job;
	//private Freq freq;
	
	private String job;
    private String freq;

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + ((job == null) ? 0 : job.hashCode());
        result = prime * result + ((freq == null) ? 0 : freq.hashCode());
        return result;
    }

	public boolean equals(Object obj) {
    	if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JobFreqPK other = (JobFreqPK) obj;
        if (job == null) {
            if (other.job != null)
                return false;
        } else if (!job.equals(other.job))
            return false;
        if (freq == null) {
            if (other.freq != null)
                return false;
        } else if (!freq.equals(other.freq))
            return false;
        return true;
    }
    
	
}
