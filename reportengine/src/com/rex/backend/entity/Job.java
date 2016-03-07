package com.rex.backend.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;

@Entity
public class Job {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    @Size(min = 2, max = 24)
    private String job_Name;
    @Size(min = 2, max = 24)
    private String job_Desc;
    
    @Size(min = 2, max = 24)
    private String job_Macro;

    
    
    public String getJob_Name() {
		return job_Name;
	}

	public void setJob_Name(String job_Name) {
		this.job_Name = job_Name;
	}

	public String getJob_Desc() {
		return job_Desc;
	}

	public void setJob_Desc(String job_Desc) {
		this.job_Desc = job_Desc;
	}

	public String getJob_Macro() {
		return job_Macro;
	}

	public void setJob_Macro(String job_Macro) {
		this.job_Macro = job_Macro;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
