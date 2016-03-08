package com.rex.backend.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.UuidGenerator;


@Entity
@UuidGenerator(name="EMP_ID_GEN")
public class JobTest {

	@Id
    @GeneratedValue(generator="EMP_ID_GEN")
    private String id;
	
	@NotNull
    @Size(min = 2, max = 24)
    private String jobName;
    @Size(min = 2, max = 24)
    private String jobDesc;
    
    @Size(min = 2, max = 24)
    private String jobMacro;

    @OneToMany(mappedBy = "jobTest")
    private Set<FreqTest> freqs;
    
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

	public Set<FreqTest> getFreqs() {
		return freqs;
	}

	public void setFreqs(Set<FreqTest> freqs) {
		this.freqs = freqs;
	}

}
