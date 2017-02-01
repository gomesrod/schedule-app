package com.javatest.scheduleapp.model;

public class Job {

	private String name;
	private String msg;
	private String cron;
		
	public Job(String name, String msg, String cron) {
		this.name = name;
		this.msg = msg;
		this.cron = cron;
	}
	
	public String getName() {
		return name;
	}
	public String getMsg() {
		return msg;
	}
	public String getCron() {
		return cron;
	}
	
	
}
