package com.ravi.exceltocsv.ExcelToCsvProject.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Data Model is created in order to bind excel elements with header.
 * @author RaviP
 *
 */
@Entity
@Table(name="DataModel")

public class DataModel {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id")
	private long id ;
	
	
	@Column(name="workItemId")
	private long workItemId;
	
	@Column(name="workItemType")
	private String workItemType;
	
	@Column(name="title")
	private String title;
	@Column(name="state")
	private String state;
	@Column(name="originalEstimate")
	private int originalEstimate;
	@Column(name="iterationPath")
	private String iterationPath;
	@Column(name="remainingWork")
	private int remainingWork;
	@Column(name="assignedTo")
	private String assignedTo;
	@Column(name="size")
	private int size;
	@Column(name="createdDate")
	private Date createdDate;
	@Column(name="parent")
	private long parent;
	
	public DataModel() {
	}

	public DataModel(long id, long workItemId, String workItemType, String title, String state, int originalEstimate,
			String iterationPath, int remainingWork, String assignedTo, int size, Date createdDate, long parent) {
		super();
		this.id = id;
		this.workItemId = workItemId;
		this.workItemType = workItemType;
		this.title = title;
		this.state = state;
		this.originalEstimate = originalEstimate;
		this.iterationPath = iterationPath;
		this.remainingWork = remainingWork;
		this.assignedTo = assignedTo;
		this.size = size;
		this.createdDate = createdDate;
		this.parent = parent;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(long workItemId) {
		this.workItemId = workItemId;
	}

	public String getWorkItemType() {
		return workItemType;
	}

	public void setWorkItemType(String workItemType) {
		this.workItemType = workItemType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getOriginalEstimate() {
		return originalEstimate;
	}

	public void setOriginalEstimate(int originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	public String getIterationPath() {
		return iterationPath;
	}

	public void setIterationPath(String iterationPath) {
		this.iterationPath = iterationPath;
	}

	public int getRemainingWork() {
		return remainingWork;
	}

	public void setRemainingWork(int remainingWork) {
		this.remainingWork = remainingWork;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "DataModel [id=" + id + ", workItemId=" + workItemId + ", workItemType=" + workItemType + ", title="
				+ title + ", state=" + state + ", originalEstimate=" + originalEstimate + ", iterationPath="
				+ iterationPath + ", remainingWork=" + remainingWork + ", assignedTo=" + assignedTo + ", size=" + size
				+ ", createdDate=" + createdDate + ", parent=" + parent + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignedTo == null) ? 0 : assignedTo.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((iterationPath == null) ? 0 : iterationPath.hashCode());
		result = prime * result + originalEstimate;
		result = prime * result + (int) (parent ^ (parent >>> 32));
		result = prime * result + remainingWork;
		result = prime * result + size;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + (int) (workItemId ^ (workItemId >>> 32));
		result = prime * result + ((workItemType == null) ? 0 : workItemType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModel other = (DataModel) obj;
		if (assignedTo == null) {
			if (other.assignedTo != null)
				return false;
		} else if (!assignedTo.equals(other.assignedTo))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (iterationPath == null) {
			if (other.iterationPath != null)
				return false;
		} else if (!iterationPath.equals(other.iterationPath))
			return false;
		if (originalEstimate != other.originalEstimate)
			return false;
		if (parent != other.parent)
			return false;
		if (remainingWork != other.remainingWork)
			return false;
		if (size != other.size)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (workItemId != other.workItemId)
			return false;
		if (workItemType == null) {
			if (other.workItemType != null)
				return false;
		} else if (!workItemType.equals(other.workItemType))
			return false;
		return true;
	}

	
	
	
	/*
	 * @Override public int hashCode() { return (int) this.workItemId; }
	 * 
	 * @Override public boolean equals(Object obj) { if(obj instanceof DataModel) {
	 * DataModel other = (DataModel) obj; if (assignedTo.equals(other.assignedTo) &&
	 * createdDate.equals(other.createdDate) &&
	 * iterationPath.equals(other.iterationPath) && originalEstimate ==
	 * other.originalEstimate && parent == other.parent && remainingWork ==
	 * other.remainingWork && size == other.size && state.equals(other.state) &&
	 * title.equals(other.title) && workItemId == other.workItemId &&
	 * workItemType.equals(other.workItemType)) return true; } return false; }
	 */
	
}
