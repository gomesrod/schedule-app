package com.javatest.scheduleapp.cron.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.javatest.scheduleapp.cron.CronEngine;
import com.javatest.scheduleapp.model.Job;

@Service
@Scope("singleton")
public class QuartzCronEngine implements CronEngine {

	private int testCounter = 0;
	
	public void start() {
		System.out.println("############################################## start()");
	}

	public void shutdown() {
		System.out.println("############################################## shutdown()");
	}

	public List<Job> listJobs() {
		System.out.println("############################################## listJobs()");
		testCounter++;
		return Arrays.asList(
				new Job("ls", "mensagem " + testCounter, "* * * *"),
				new Job("du", "msg2", "* 10 20 *")
		);
	}

	public void addJob(Job job) {
		System.out.println("############################################## addJob()");
	}

	public void deleteJob(Job job) {
		System.out.println("############################################## deleteJob()");		
	}

}
