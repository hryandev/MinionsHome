package com.rex.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.UuidGenerator;

@Entity
@UuidGenerator(name="EMP_ID_GEN")
public class FreqTest {

	@Id
    @GeneratedValue(generator="EMP_ID_GEN")
    private String id;
	
	@NotNull
    private String freqName;
    private String freqDesc;

    @ManyToOne
    private JobTest jobTest;
    
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

	public JobTest getJobTest() {
		return jobTest;
	}

	public void setJobTest(JobTest jobTest) {
		this.jobTest = jobTest;
	}

}
