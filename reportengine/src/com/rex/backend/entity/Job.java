package com.rex.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;

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
	
	@ObjectTypeConverter(name = "stringToBooleanConverter", dataType = java.lang.String.class, objectType = java.lang.Boolean.class, defaultObjectValue = "false", conversionValues = {
			@ConversionValue(dataValue = "F", objectValue = "false"),
			@ConversionValue(dataValue = "T", objectValue = "true") 
	})
	
	@ManyToMany()
    @JoinTable(name = "JOBFREQ",joinColumns = @JoinColumn(name = "JOB_ID"),inverseJoinColumns = @JoinColumn(name = "FREQ_ID"))
    private List<Freq> freqs = new ArrayList<>();
	
	//@OneToMany(mappedBy="job",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	//private List<JobFreq> jfRelation = new ArrayList<>();
	
	@Convert("stringToBooleanConverter")
	@Column(name = "JOB_ACTIVATE")
    private Object activate;
	
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
	
	public Object getActivate() {
		return activate;
	}

	public void setActivate(Object activate) {
		this.activate = activate;
	}
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(jobName);
        return hcb.toHashCode();
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Job)) {
            return false;
        }
        Job that = (Job) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(id, that.id);
        return eb.isEquals();
    }
	
}
