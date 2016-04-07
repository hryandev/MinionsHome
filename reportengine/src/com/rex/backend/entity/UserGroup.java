package com.rex.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "USERGROUP")
@IdClass(GroupPK.class)
public class UserGroup {
	@Id
	@Column(name = "JOB_ID")
	private String jobID;

	@Id
	@Column(name = "USER_ID")
	private String userID;
	
	@Column(name = "MAIL_TOCC")
	private String tocc;

	
	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getTocc() {
		return tocc;
	}

	public void setTocc(String tocc) {
		this.tocc = tocc;
	}

}
