package com.javatest.scheduleapp.service;

/**
 * Representa uma resposta padrao para requisicoes, contendo confirmacao de sucesso e 
 * mensagem de erro, se aplicavel.
 * 
 * Ex:
 * {success:true, message:""}
 * {success:false, message:"Dados incorretos"}
 * 
 * @author rodrigo
 *
 */
public class SimpleResponse {

	private boolean success;
	private String message;
	
	public SimpleResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
	
	
}
