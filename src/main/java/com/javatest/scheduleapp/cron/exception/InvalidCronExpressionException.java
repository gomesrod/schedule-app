package com.javatest.scheduleapp.cron.exception;

/**
 * Indica que o formato da string de especificação de schedule é inválida. 
 * @author rodrigo
 *
 */
public class InvalidCronExpressionException extends Exception {

	private static final long serialVersionUID = -25827911230504351L;

	public InvalidCronExpressionException(String message) {
		super(message);
	}
		
}
