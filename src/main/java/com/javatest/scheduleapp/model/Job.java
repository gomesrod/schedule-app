package com.javatest.scheduleapp.model;

/**
 * Representa um job no mecanismo de agendamento.
 * @author rodrigo
 *
 */
public class Job {

	/**
	 * Identificação do Job
	 */
	private String name;
	
	/**
	 * Mensagem associada ao job. Ela sera impressa nos horarios agendados.
	 */
	private String msg;
	
	/**
	 * Especificação de agendamento. Utiliza o formato do crontab Unix.
	 */
	private String cron;

	/**
	 * @param name
	 * @param msg
	 * @param cron
	 */
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

	@Override
	public String toString() {
		return "Job [name=" + name + ", msg=" + msg + ", cron=" + cron + "]";
	}
}
