package com.rex.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
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
	@Column(name = "JOB_ID")
    private String id = "";
	
	@NotNull
	@Column(name = "JOB_NAME")
    private String jobName = "";
	
	@Column(name = "JOB_DESC")
    private String jobDesc = "";
    
	@Column(name = "JOB_MACRO")
    private String jobMacro = "";
	
	@ManyToMany()
    @JoinTable(name = "JOBFREQ",joinColumns = @JoinColumn(name = "JOB_ID"),inverseJoinColumns = @JoinColumn(name = "FREQ_ID"))
    private List<Freq> freqs = new ArrayList<>();
	
	//@OneToMany(mappedBy="job",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	//private List<JobFreq> jfRelation = new ArrayList<>();
	
	@ObjectTypeConverter(name = "stringToBooleanConverter", dataType = java.lang.String.class, objectType = java.lang.Boolean.class, defaultObjectValue = "false", conversionValues = {
			@ConversionValue(dataValue = "F", objectValue = "false"),
			@ConversionValue(dataValue = "T", objectValue = "true") 
	})
	
	@Convert("stringToBooleanConverter")
	@Column(name = "JOB_ACTIV")
    private Object activate;
	
	@Column(name = "JOB_FLAG")
    private String flag;

	@Column(name = "JOB_QTM")
    private int jobQtm;

	@Column(name = "JOB_MLSBT")
    private String mailSubject = "";
	
	@Column(name = "JOB_MLBDY")
    private String mailBody = "";
	
	public Job(){
		this.id = UUID.randomUUID().toString();
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	public int getJobQtm() {
		return jobQtm;
	}

	public void setJobQtm(int jobQtm) {
		this.jobQtm = jobQtm;
	}
	
	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
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
