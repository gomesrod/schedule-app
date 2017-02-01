package com.javatest.scheduleapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javatest.scheduleapp.cron.CronEngine;
import com.javatest.scheduleapp.model.Job;

@RestController
public class JobScheduleService {

	@Autowired
	private CronEngine cronEngine;
	
	@RequestMapping(value="/jobs", produces="application/json")
	public List<Job> listJobs() {
		List<Job> jobs = cronEngine.listJobs();
		return jobs;
	}
}
