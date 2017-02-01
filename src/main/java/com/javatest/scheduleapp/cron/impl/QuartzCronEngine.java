package com.javatest.scheduleapp.cron.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.javatest.scheduleapp.cron.CronEngine;
import com.javatest.scheduleapp.model.Job;

@Service
@Scope("singleton")
public class QuartzCronEngine implements CronEngine {

	private Scheduler scheduler;

	@PostConstruct
	public void init() throws SchedulerException {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		this.scheduler = schedFact.getScheduler();
	}

	public void start() {
		System.out.println("############################################## start()");

		try {
			scheduler.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

	}

	public void shutdown() {
		System.out.println("############################################## shutdown schedule ()");
		try {
			this.scheduler.shutdown(true);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Job> listJobs() {
		System.out.println("############################################## listJobs()");
		return Arrays.asList(new Job("ls", "mensagem ", "* * * *"), new Job("du", "msg2", "* 10 20 *"));
	}

	public void addJob(Job job) {
		System.out.println("############################################## addJob()");
		// define the job and tie it to our HelloJob class
		JobDetail quartzJob = JobBuilder.newJob(MessagePrintJob.class).withIdentity("myJob", "group1").build();

		// Trigger the job to run now, and then every 40 seconds
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(quartzJob, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteJob(Job job) {
		System.out.println("############################################## deleteJob()");
	}

}
