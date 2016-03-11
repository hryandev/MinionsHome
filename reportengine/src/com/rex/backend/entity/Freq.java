package com.rex.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.UuidGenerator;

@Entity
@Table(name="FREQ")
@UuidGenerator(name="EMP_ID_GEN")
public class Freq {

	@Id
    @GeneratedValue(generator="EMP_ID_GEN")
	@Column(name = "FREQ_ID")
    private String id;
	
	@NotNull
	@Column(name = "FREQ_NAME")
    private String freqName;
	
	@Column(name = "FREQ_DESC")
    private String freqDesc;
	
	//@OneToMany(mappedBy = "freq", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    //private List<JobFreq> jfRelation = new ArrayList<>();

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


}
