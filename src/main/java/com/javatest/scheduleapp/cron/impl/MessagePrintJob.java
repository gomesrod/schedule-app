package com.javatest.scheduleapp.cron.impl;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MessagePrintJob implements Job {

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! JOB " + new Date());
	}

}
