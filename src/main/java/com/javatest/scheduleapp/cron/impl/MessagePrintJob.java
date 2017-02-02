package com.javatest.scheduleapp.cron.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job que tem a finalidade de imprimir a mensagem recebida através do data map.
 * 
 * Nota de implementacao: O enunciado pede que seja chamado 
 * System.out.println no parametro "msg"
 * 
 * Este procedimento foi substituido por uma chamada ao logger.info() ; o motivo
 * é que alguns containers (ex: Jetty) suprimem as mensagens do System.out, tornando
 * impossivel acompanhar o resultado da execução.
 * 
 * @author rodrigo
 *
 */
public class MessagePrintJob implements Job {
	
	private static final String JOB_MESSAGE_KEY = "jobMessage";
	private static final Logger logger = LoggerFactory.getLogger("PRINT-JOB");

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		String message = ctx.getJobDetail().getJobDataMap().getString(JOB_MESSAGE_KEY);
		
		logger.info(message);
	}

}
