package com.rex.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Date;

public class Contact implements Serializable, Cloneable {

    private Long id;

    private String jobName = "";
    private String jobDesc = "";
    private String jobMacro = "";
    private int jobQuantum = 0;
    private Date jobFreq;

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

	public int getJobQuantum() {
		return jobQuantum;
	}

	public void setJobQuantum(int jobQuantum) {
		this.jobQuantum = jobQuantum;
	}

	public Date getJobFreq() {
		return jobFreq;
	}

	public void setJobFreq(Date jobFreq) {
		this.jobFreq = jobFreq;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public Contact clone() throws CloneNotSupportedException {
        try {
            return (Contact) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Contact{" + "id=" + id + ", jobName=" + jobName
                + ", jobDesc=" + jobDesc + ", jobMacro=" + jobMacro + ", jobQuantum="
                + jobQuantum + ", jobFreq=" + jobFreq + '}';
    }

}
