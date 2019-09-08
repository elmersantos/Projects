package com.project.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
	
	private long taskID;
	private long projectID;
	private String taskName;
	private Date startDate;
	private Date endDate;
	private int daysDuration;
	private List<Task> subTasks =  new ArrayList<Task>();

	public Task() {	}
	
	public Task(long projectID, String taskName) {
		super();
		this.projectID = projectID;
		this.taskName = taskName;
	}
	
	public Task(String taskName) {
		super();
		this.taskName = taskName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
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
	public int getDaysDuration() {
		return daysDuration;
	}
	public void setDaysDuration(int daysDuration) {
		this.daysDuration = daysDuration;
	}
	public List<Task> getSubTasks() {
		return subTasks;
	}
	public void setSubTasks(List<Task> subTasks) {
		this.subTasks = subTasks;
	}
	public void addTask(Task task) {
		this.subTasks.add(task);
	}
	public long getTaskID() {
		return taskID;
	}
	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}
	public long getProjectID() {
		return projectID;
	}
	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	@Override
	public String toString() {
		return taskName;
	}
	
}
