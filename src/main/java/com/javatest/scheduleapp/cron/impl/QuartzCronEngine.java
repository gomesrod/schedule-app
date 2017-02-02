package com.javatest.scheduleapp.cron.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.javatest.scheduleapp.cron.CronEngine;
import com.javatest.scheduleapp.cron.exception.JobAlreadyExistsException;
import com.javatest.scheduleapp.cron.exception.NoSuchJobException;
import com.javatest.scheduleapp.model.Job;

/**
 * Implementação de {@link CronEngine} baseado no Quartz.
 * 
 * OBS: Os jobs por padrao nao sao persistetes, pois o Quartz opera com o jobstore "RAM"
 *      Em uma aplicacao real de producao seria desejavel persistir os Jobs, para isso
 *      basta configurar o Quartz com um jobstore em banco de dados.
 *      
 * @author rodrigo
 */
@Service
@Scope("singleton")
public class QuartzCronEngine implements CronEngine {

	private static final String JOB_CRON_KEY = "jobCron";
	private static final String JOB_MESSAGE_KEY = "jobMessage";
	private static final String JOB_NAME_KEY = "jobName";
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzCronEngine.class);
	private static final String JOB_GROUP = "cron-engine";
	
	private Scheduler scheduler;

	@PostConstruct
	public void init() throws SchedulerException {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		this.scheduler = schedFact.getScheduler();
	}

	/*
	 * (non-Javadoc)
	 * @see com.javatest.scheduleapp.cron.CronEngine#start()
	 */
	public void start() {
		logger.info("QuartzCronEngine is starting");

		try {
			scheduler.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.javatest.scheduleapp.cron.CronEngine#shutdown()
	 */
	public void shutdown() {
		logger.info("QuartzCronEngine is shutting down");
		try {
			this.scheduler.shutdown(true);
			logger.info("Shutdown complete");
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.javatest.scheduleapp.cron.CronEngine#addJob(com.javatest.scheduleapp.model.Job)
	 */
	public void addJob(Job job) throws JobAlreadyExistsException {
		logger.debug("Entering addJob() " + job);
		
		JobKey jobKey = new JobKey(job.getName(), JOB_GROUP);

		try {
			if (scheduler.checkExists(jobKey)) {
				throw new JobAlreadyExistsException("Job with name [" + job.getName() + "] already exists");
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		
		JobDetail quartzJob = JobBuilder.newJob(MessagePrintJob.class)
				.withIdentity(job.getName(), JOB_GROUP)
				.usingJobData(JOB_NAME_KEY, job.getName())
				.usingJobData(JOB_MESSAGE_KEY, job.getMsg())
				.usingJobData(JOB_CRON_KEY, job.getCron())
				.build();

		// Trigger the job to run now, and then every 40 seconds
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(job.getName(), JOB_GROUP)
				.startNow()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(5).repeatForever())
				.build();

		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(quartzJob, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.javatest.scheduleapp.cron.CronEngine#listJobs()
	 */
	public List<Job> listJobs() {
		logger.debug("Entering listJobs()");
		
		Set<JobKey> jobKeys;
		try {
			jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP));
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		
		// Maps a list of jobKeys to a list of Job objects.
		List<Job> result = jobKeys.stream().map(jobKey -> {
				JobDataMap dataMap;
				try {
					dataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
				} catch (SchedulerException e) {
					throw new RuntimeException(e);
				}
				Job job = new Job(dataMap.getString(JOB_NAME_KEY), 
						dataMap.getString(JOB_MESSAGE_KEY),
						dataMap.getString(JOB_CRON_KEY));
				return job;
			}).collect(Collectors.toList());
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.javatest.scheduleapp.cron.CronEngine#deleteJob(java.lang.String)
	 */
	public void deleteJob(String jobName) throws NoSuchJobException {
		logger.debug("Entering deleteJob() " + jobName);
		
		JobKey jobKey = new JobKey(jobName, JOB_GROUP);

		try {
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			} else {
				throw new NoSuchJobException("O job [" + jobName + "] nao existe");
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

}
