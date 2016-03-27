package com.rex.backend.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Task {
	
	@Id
	@Column(name = "TASK_ID")
    private String id;
	
	@Column(name = "TASK_JOBID")
    private String jobID;
	
	@Column(name = "TASK_PID")
    private String taskPID = "";
	
	@Column(name = "TASK_NAME")
    private String taskName = "";
	
	@Column(name = "TASK_STATUS")
    private String taskStatus = "";
	
	@Column(name="TASK_START")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date startTime;
	
	@Column(name="TASK_END")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endTime;
	
	@Column(name = "TASK_QTM")
    private int taskQtm;
	
	@Column(name = "TASK_FILE")
    private String taskFile = "";
	
	@ManyToOne()
	@PrimaryKeyJoinColumn(name="TASK_JOBID")
	private Job job;

	public Task(){
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getTaskPID() {
		return taskPID;
	}

	public void setTaskPID(String taskPID) {
		this.taskPID = taskPID;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public int getTaskQtm() {
		return taskQtm;
	}

	public void setTaskQtm(int taskQtm) {
		this.taskQtm = taskQtm;
	}

	public String getTaskFile() {
		return taskFile;
	}

	public void setTaskFile(String taskFile) {
		this.taskFile = taskFile;
	}
	
	
	
	
}
