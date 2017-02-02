package com.javatest.scheduleapp.cron.impl;

import java.text.ParseException;

import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import com.javatest.scheduleapp.cron.exception.InvalidCronExpressionException;

/**
 * Responsável pela transformação de Cron Specification do formato unix (entrada do sistem)
 * para o formato específico do Quartz.
 * <br />
 * Este processo de transformação consiste em:<br />
 * - Validar se a expressão de entrada possui o número correto de tokens (5), conforme o padrão unix crontab.<br />
 * - Trata parâmetros day-of-week e day-of-month. Eles não podem estar ambos presentes devido a característica de implementação do quartz<br />
 * - Adicionar um token adicional no início, Seconds = 0   (exclusivo do formato cronexpression do Quartz)<br />
 * - Adicionar um token adicional no fim, Year = *   (exclusivo do formato cronexpression do Quartz)<br />
 * - Fazer parse utilizando a classe CronExpression do quartz. Erros são repassados em forma de InvalidCronExpressionException<br />
 * 
 * @author T3276780
 */
@Component
public class CronExpressionAdapter {

	private static final int INDEX_DAY_OF_MONTH = 2;
	private static final int INDEX_DAY_OF_WEEK = 4;
	
	/**
	 * 
	 * @param crontab
	 * @return
	 */
	public CronExpression fromUnixCronExpression(String crontab) throws InvalidCronExpressionException {
		String[] tokens = tokenize(crontab);
		
		handleDaysCombination(tokens);
		
		CronExpression resultExpression;
		try {
			String expressionWithAdditionalTokens = "0 " + String.join(" ", tokens) + " *";
			resultExpression = new CronExpression(expressionWithAdditionalTokens);
		} catch (ParseException e) {
			throw new InvalidCronExpressionException(e.getMessage());
		}
		return resultExpression;
	}

	private void handleDaysCombination(String[] tokens) throws InvalidCronExpressionException {
		// Devido a uma limitação do Quartz não pode ser passado um valor para ambos os parâmetros
		// Dia do mês e Dia da Semana (mesmo que seja * em ambos). Um deles deve estar como '?' = não especificado
		// Portanto é feito o seguinte tratamento:
		// - Se foi passado um valor para o dia da semana, transforma o dia do mês em '?'
		// - Se foi passado um valor para o dia do mês, transforma o dia da semana em '?'
		// - Se estão ambos com asterisco, transforma o dia da semana em '?'
		// - Se foi passado um valor para os dois campos retorna erro de expressão, pois a situação não é permitida
		
		if (isActualValue(tokens[INDEX_DAY_OF_WEEK]) && !isActualValue(tokens[INDEX_DAY_OF_MONTH])) {
			tokens[INDEX_DAY_OF_MONTH] = "?";
		} else if (isActualValue(tokens[INDEX_DAY_OF_WEEK]) && isActualValue(tokens[INDEX_DAY_OF_MONTH])) {
			throw new InvalidCronExpressionException("It is not allowed to include both day-of-month and day-of-week");
		} else {
			tokens[INDEX_DAY_OF_WEEK] = "?";
		}
	}

	/**
	 * Informa se o token é um valor real - ou seja, não é '*'
	 * @param token
	 * @return
	 */
	private boolean isActualValue(String token) {
		return !"*".equals(token) && !"?".equals(token);
	}

	/**
	 * Divide em tokens e garante que tera exatamente 5 partes, que sao os campos aceitos em expressoes da crontab do Unix
	 * @param crontab
	 * @return
	 * @throws InvalidCronExpressionException 
	 */
	private String[] tokenize(String crontab) throws InvalidCronExpressionException {
		String[] tokens = crontab.split(" ");
		
		if (tokens.length != 5) {
			throw new InvalidCronExpressionException("Invalid crontab expression. There must be 5 tokens");
		}
		
		return tokens;
	}

}
