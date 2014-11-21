package com.sugardefynery.animeconvention.scheduler.Alerts;

public class Alerts {

	private int rowId;
	private int remoteServerId;
	private int eventState;
	private int alertState;
	private String eventName;
	private String startTime;
	private String alertTime;
	private String location;
	private String date;
	private String minutes;
	private String origAlertDate;
	private String alertInMillis;
	private String description;
	private int alarmDismissed;

	
	public int getAlarmDismissed() {
		return alarmDismissed;
	}

	public void setAlarmDismissed(int alarmDismissed) {
		this.alarmDismissed = alarmDismissed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int row_id) {
		this.rowId = row_id;
	}

	public int getRemoteServerId() {
		return remoteServerId;
	}

	public void setRemoteServerId(int remote_Server_id) {
		this.remoteServerId = remote_Server_id;
	}

	public int getEventState() {
		return eventState;
	}

	public void setEventState(int event_State) {
		this.eventState = event_State;
	}

	public int getAlertState() {
		return alertState;
	}

	public void setAlertState(int alert_State) {
		this.alertState = alert_State;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String event_Name) {
		this.eventName = event_Name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location_) {
		this.location = location_;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String start_Time) {
		this.startTime = start_Time;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String start_Time) {
		this.alertTime = start_Time;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date_) {
		this.date = date_;
	}
	
	public String getAlertMinutes() {
		return minutes;
	}

	public void setAlertMinutes(String minutes) {
		this.minutes = minutes;
	}
	
	public String getOrgAlertDate() {
		return origAlertDate;
	}

	public void setOrgAlertDate(String origAlertDate) {
		this.origAlertDate = origAlertDate;
	}

	public void  setAlertInMillis(String alertInMillis) {
		 this.alertInMillis = alertInMillis;
		
	}
	
	public String getAlertInMillis() {
		return alertInMillis;
	}
}
