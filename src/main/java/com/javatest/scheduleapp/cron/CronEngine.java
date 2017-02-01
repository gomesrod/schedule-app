package com.javatest.scheduleapp.cron;

import java.util.List;

import com.javatest.scheduleapp.model.Job;

/**
 * Interface principal para o mecanismo de execu��o de tarefas agendadas.
 * Esta interface � neutra quanto � forma de implementa��o do agendamento.
 * 
 * (poderiam ser usadas por exemplo implementa��es baseadas em Quartz, EJB Timer, 
 * ExecutorService, Crontab, etc, sem impacto para a interface)
 * 
 * @author rodrigo
 */
public interface CronEngine {
	/**
	 * Inicia a execu��o dos agendamentos. Deve ser acionado na inicializa��o da aplica��o.
	 */
	public void start();
	
	/**
	 * Finaliza a execu��o dos agendamentos. Deve ser acionado no encerramento da aplica��o.
	 */
	public void shutdown();
	
	/**
	 * Lista os jobs 
	 * @return
	 */
	public List<Job> listJobs();
	public void addJob(Job job);
	public void deleteJob(Job job);
}
