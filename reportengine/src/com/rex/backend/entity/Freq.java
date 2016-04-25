package com.rex.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@Entity
public class Freq {

	@Id
	@Column(name = "FREQ_ID")
    private String id;
	
	@NotNull
	@Column(name = "FREQ_NAME")
    private String freqName = "";
	
	@Column(name = "FREQ_DESC")
    private String freqDesc = "";
	
	@Column(name = "FREQ_TYPE")
    private String freqType = "";
	
	@Column(name="FREQ_START")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "FREQ_INTVL")
    private int interval = 1;
	
	@Column(name = "FREQ_CNT")
    private int repeat = 0;
	
	//@OneToMany(mappedBy = "freq", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    //private List<JobFreq> jfRelation = new ArrayList<>();

	@ManyToMany()
    @JoinTable(name = "JOBFREQ",joinColumns = @JoinColumn(name = "FREQ_ID"),inverseJoinColumns = @JoinColumn(name = "JOB_ID"))
    private List<Job> jobList = new ArrayList<>();
	
	public Freq(){
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFreqName() {
		return freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

	public String getFreqDesc() {
		return freqDesc;
	}

	public void setFreqDesc(String freqDesc) {
		this.freqDesc = freqDesc;
	}
	
	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(freqName);
        return hcb.toHashCode();
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Freq)) {
            return false;
        }
        Freq that = (Freq) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(id, that.id);
        return eb.isEquals();
    }
}
