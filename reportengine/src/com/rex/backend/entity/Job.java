package com.rex.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Job {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "JOB_ID")
    private String id;
	
	@NotNull
	@Column(name = "JOB_NAME")
    private String jobName;
	
	@Column(name = "JOB_DESC")
    private String jobDesc;
    
	@Column(name = "JOB_MACRO")
    private String jobMacro;
	
	@ManyToMany()
    @JoinTable(name = "JOBFREQ",joinColumns = @JoinColumn(name = "JOB_ID"),inverseJoinColumns = @JoinColumn(name = "FREQ_ID"))
    private List<Freq> freqs;
	
	//@OneToMany(mappedBy="job",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	//private List<JobFreq> jfRelation = new ArrayList<>();

	
	public Job(){
		
	}
	
	public Job(String id , String name, String macro){
		this.id = id;
		jobName = name;
		jobMacro = macro;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public String getJobMacro() {
		return jobMacro;
	}

	public void setJobMacro(String jobMacro) {
		this.jobMacro = jobMacro;
	}

	public List<Freq> getFreqs() {
		return freqs;
	}

	public void setFreqs(List<Freq> freqs) {
		this.freqs = freqs;
	}
    
	public void addFreq(Freq freq) {
	      if (!getFreqs().contains(freq)) {
	          getFreqs().add(freq);
	      }
	}
	
}
