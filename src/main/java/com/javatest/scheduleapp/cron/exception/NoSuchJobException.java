package com.javatest.scheduleapp.cron.exception;

/**
 * Utilizada para indicar que a acao solicitada nao pode ser realizada porque o Job
 * nao existe. 
 * @author rodrigo
 *
 */
public class NoSuchJobException extends Exception {

	private static final long serialVersionUID = -2582791773530504351L;

	public NoSuchJobException(String message) {
		super(message);
	}
		
}
