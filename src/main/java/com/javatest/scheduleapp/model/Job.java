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
	
	/**
	 * 
	 */
	public Job() {		
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cron == null) ? 0 : cron.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (cron == null) {
			if (other.cron != null)
				return false;
		} else if (!cron.equals(other.cron))
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
