package com.rex.backend.entity;

import java.io.Serializable;

import javax.persistence.IdClass;

@IdClass(GroupPK.class)
public class GroupPK implements Serializable{
	
	private String jobID;
	private String userID;
	
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
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof GroupPK){
			GroupPK pk = (GroupPK)obj;
			if(this.jobID == pk.getJobID() && this.userID.equals(pk.getUserID())){
				return true;
			}else{
				return false;
			}
		}
		
		return false;

	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
}
