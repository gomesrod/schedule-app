package com.javatest.scheduleapp.cron;

import java.util.List;

import com.javatest.scheduleapp.model.Job;

public interface CronEngine {
	public void start();
	public void shutdown();
	
	public List<Job> listJobs();
	public void addJob(Job job);
	public void deleteJob(Job job);
}
