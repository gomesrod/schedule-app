package com.javatest.scheduleapp.cron.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.quartz.CronExpression;

import com.javatest.scheduleapp.cron.exception.InvalidCronExpressionException;

/**
 * Testes unitários para CronExpressionAdapter
 * @author T3276780
 *
 */
public class CronExpressionAdapterTest {

	private CronExpressionAdapter cronExpressionAdapter = new CronExpressionAdapter();

	@Test
	public void validExpression_allAsterisks() throws InvalidCronExpressionException {
		CronExpression cronExpr = cronExpressionAdapter.fromUnixCronExpression("* * * * *");
 
		// O dia da semana foi alterado de * (qualquer) para ? (nao especificado)
		assertNotNull(cronExpr);
		assertEquals("0 * * * * ? *", cronExpr.getCronExpression());
	}
	
	@Test
	public void validExpression_withDayOfMonth() throws InvalidCronExpressionException {
		CronExpression cronExpr = cronExpressionAdapter.fromUnixCronExpression("0 1 10 5 *");
 
		// O dia da semana foi alterado de * (qualquer) para ? (nao especificado)
		// Nao pode ser passado um valor para ambos os parametros, mesmo que seja *
		assertNotNull(cronExpr);
		assertEquals("0 0 1 10 5 ? *", cronExpr.getCronExpression());
	}
	
	@Test
	public void validExpression_withDayOfWeek() throws InvalidCronExpressionException {
		CronExpression cronExpr = cronExpressionAdapter.fromUnixCronExpression("0 1 * 5 SUN,MON");
 
		// Como especificamos um valor no Dia da Semana, então 
		// o dia do mês foi alterado de * (qualquer) para ? (nao especificado)
		assertNotNull(cronExpr);
		assertEquals("0 0 1 ? 5 SUN,MON *", cronExpr.getCronExpression());
	}
	
	@Test(expected=InvalidCronExpressionException.class)
	public void invalidExpression_wrongNumberOfTokens_lessThan5() throws InvalidCronExpressionException {
		cronExpressionAdapter.fromUnixCronExpression("* * * *");
	}
	
	@Test(expected=InvalidCronExpressionException.class)
	public void invalidExpression_wrongNumberOfTokens_moreThan5() throws InvalidCronExpressionException {
		cronExpressionAdapter.fromUnixCronExpression("* * * * * *");
	}
	
	/**
	 * Teste do caso em que ambos os parametros day-of-month e day-of-week sao informados.
	 * Esta situação não é permitida.
	 */
	@Test(expected=InvalidCronExpressionException.class)
	public void invalidExpression_containsBothDayParameters() throws InvalidCronExpressionException {
		cronExpressionAdapter.fromUnixCronExpression("* * 2 * 3");
	}
	
	@Test(expected=InvalidCronExpressionException.class)
	public void invalidExpression_illegalContents() throws InvalidCronExpressionException {
		cronExpressionAdapter.fromUnixCronExpression("* A * * *");
	}
}
