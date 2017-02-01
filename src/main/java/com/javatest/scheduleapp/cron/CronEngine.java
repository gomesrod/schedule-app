package com.javatest.scheduleapp.cron;

import java.util.List;

import com.javatest.scheduleapp.model.Job;

/**
 * Interface principal para o mecanismo de execução de tarefas agendadas.
 * Esta interface é neutra quanto à forma de implementação do agendamento.
 * 
 * (poderiam ser usadas por exemplo implementações baseadas em Quartz, EJB Timer, 
 * ExecutorService, Crontab, etc, sem impacto para a interface)
 * 
 * @author rodrigo
 */
public interface CronEngine {
	/**
	 * Inicia a execução dos agendamentos. Deve ser acionado na inicialização da aplicação.
	 */
	public void start();
	
	/**
	 * Finaliza a execução dos agendamentos. Deve ser acionado no encerramento da aplicação.
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
