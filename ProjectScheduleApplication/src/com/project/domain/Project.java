package com.project.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
	
	private long projID;
	private String projName;
	private int daysDuration;
	private Date startDate;
	private Date endDate;
	private List<Task> tasks = new ArrayList<Task>();
	
	public Project() { }
	
	public Project(String projName) {
		super();
		this.setProjName(projName);
	}
	
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public int getDaysDuration() {
		return daysDuration;
	}
	public void setDaysDuration(int daysDuration) {
		this.daysDuration = daysDuration;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getProjID() {
		return projID;
	}

	public void setProjID(long projID) {
		this.projID = projID;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return projName;
	}
	
}
