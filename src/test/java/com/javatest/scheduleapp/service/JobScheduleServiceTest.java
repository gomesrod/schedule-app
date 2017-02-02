package com.javatest.scheduleapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.javatest.scheduleapp.cron.CronEngine;
import com.javatest.scheduleapp.cron.exception.InvalidCronExpressionException;
import com.javatest.scheduleapp.cron.exception.JobAlreadyExistsException;
import com.javatest.scheduleapp.cron.exception.NoSuchJobException;
import com.javatest.scheduleapp.model.Job;

/**
 * Testes unitários da implementação dos webservices de agendamento
 * @author T3276780
 *
 */
public class JobScheduleServiceTest {

	private JobScheduleService scheduleService;
	private CronEngine cronEngineMock;
	
	@Before
	public void init() {
		scheduleService = new JobScheduleService();
		cronEngineMock = Mockito.mock(CronEngine.class);
		scheduleService.setCronEngine(cronEngineMock);
	}
	
	@Test
	public void listJobs() {
		Job job0 = new Job("First Job", "Some message 1", "10 * * * *");
		Job job1 = new Job("Second Job", "Some message 2", "20 * * * *");
		Job job2 = new Job("Third Job", "Some message 3", "30 * * * *");
		
		Mockito.when(cronEngineMock.listJobs())
			.thenReturn(Arrays.asList(job0, job1, job2));
		
		List<Job> result = scheduleService.list();
		
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(job0, result.get(0));
		assertEquals(job1, result.get(1));
		assertEquals(job2, result.get(2));
	}
	
	@Test
	public void create_success() throws JobAlreadyExistsException, InvalidCronExpressionException {
		String body = "{\"name\": \"Job1\", \"msg\": \"My Message\", \"cron\": \"0 * * * *\"}";
		
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		SimpleResponse result = scheduleService.create(body, httpResponse);
		
		Mockito.verify(cronEngineMock).addJob(new Job("Job1", "My Message", "0 * * * *"));
		
		assertTrue(result.isSuccess());
		assertEquals("Job created successfully", result.getMessage());
	}
	
	@Test
	public void create_malformedJson() {
		String body = "{SOME MALFORMED JSON DATA}";
		
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		SimpleResponse result = scheduleService.create(body, httpResponse);

		Mockito.verify(httpResponse).setStatus(400); // BAD REQUEST
		
		assertFalse(result.isSuccess());
		assertNotNull(result.getMessage());
		assertTrue(result.getMessage().startsWith("Malformed JSON in request data"));
	}
	
	@Test
	public void create_missingAttribute_name() throws JobAlreadyExistsException {
		String body = "{\"msg\": \"My Message\", \"cron\": \"0 * * * *\"}";
		
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		SimpleResponse result = scheduleService.create(body, httpResponse);

		Mockito.verify(httpResponse).setStatus(400); // BAD REQUEST
		
		assertFalse(result.isSuccess());
		assertNotNull(result.getMessage());
		assertEquals("Missing 'name' attribute in input data", result.getMessage());
	}
	
	@Test
	public void create_missingAttribute_msg() {
		String body = "{\"name\": \"Job1\", \"cron\": \"0 * * * *\"}";
		
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		SimpleResponse result = scheduleService.create(body, httpResponse);

		Mockito.verify(httpResponse).setStatus(400); // BAD REQUEST
		
		assertFalse(result.isSuccess());
		assertNotNull(result.getMessage());
		assertEquals("Missing 'msg' attribute in input data", result.getMessage());
	}
	
	@Test
	public void create_missingAttribute_cron() {
		String body = "{\"name\": \"Job1\", \"msg\": \"My Message\" }";
		
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		SimpleResponse result = scheduleService.create(body, httpResponse);

		Mockito.verify(httpResponse).setStatus(400); // BAD REQUEST
		
		assertFalse(result.isSuccess());
		assertNotNull(result.getMessage());
		assertEquals("Missing 'cron' attribute in input data", result.getMessage());
	}
	
	@Test
	public void create_duplicatedJobError() throws JobAlreadyExistsException, InvalidCronExpressionException {
		String body = "{\"name\": \"Job1\", \"msg\": \"My Message\", \"cron\": \"0 * * * *\"}";
		
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		Mockito.doThrow(new JobAlreadyExistsException("Job already exists")).when(cronEngineMock).addJob(Mockito.any());
		
		SimpleResponse result = scheduleService.create(body, httpResponse);		
		
		Mockito.verify(httpResponse).setStatus(400); // BAD REQUEST		
		assertFalse(result.isSuccess());
		assertNotNull(result.getMessage());
		assertEquals("Job already exists", result.getMessage());
	}
	
	@Test
	public void delete_success() throws NoSuchJobException {	
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		
		SimpleResponse result = scheduleService.delete("job1", httpResponse);
		
		Mockito.verify(cronEngineMock).deleteJob("job1");
		
		assertTrue(result.isSuccess());
		assertEquals("Job deleted successfully", result.getMessage());
	}
	
	@Test
	public void delete_jobDoesNotExist() throws NoSuchJobException {	
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
				
		Mockito.doThrow(new NoSuchJobException("Job does not exist")).when(cronEngineMock).deleteJob(Mockito.anyString());
		
		SimpleResponse result = scheduleService.delete("job1", httpResponse);
				
		Mockito.verify(httpResponse).setStatus(404); // NOT FOUND
		assertFalse(result.isSuccess());
		assertNotNull(result.getMessage());
		assertEquals("Job does not exist", result.getMessage());
	}
}
