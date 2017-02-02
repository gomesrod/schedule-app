package com.javatest.scheduleapp.cron.exception;

/**
 * Utilizada para indicar que a inclusão nao pode ser realizada porque ja
 * existe um job criado com o mesmo nome.
 *  
 * @author rodrigo
 *
 */
public class JobAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2582791773530504351L;

	public JobAlreadyExistsException(String message) {
		super(message);
	}
		
}
