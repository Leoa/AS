package com.sugardefynery.animeconvention.scheduler.EventList;


public class Item {
	private int id;
	private String name;
	private String date;
	private String startTime;
	private String endTime;
	private String location;
	private int deleteFlag;
	private String undate;
	private String description;
	private long compareDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getUprocessedDate() {
		return undate;
	}
	public void setUprocessedDate(String undate) {
		this.undate = undate;
	}
	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartTimeCompare(long compareDate) {
		this.compareDate = compareDate;
		
	}
	
	public long getStartTimeCompare() {
		return compareDate;
	}
	
}
