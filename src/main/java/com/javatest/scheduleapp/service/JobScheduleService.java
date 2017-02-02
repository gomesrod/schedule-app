package com.javatest.scheduleapp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatest.scheduleapp.cron.CronEngine;
import com.javatest.scheduleapp.cron.exception.InvalidCronExpressionException;
import com.javatest.scheduleapp.cron.exception.JobAlreadyExistsException;
import com.javatest.scheduleapp.cron.exception.NoSuchJobException;
import com.javatest.scheduleapp.model.Job;

/**
 * Interface REST para o serviço de agendamento.
 * @author rodrigo
 *
 */
@RestController
public class JobScheduleService {

	@Autowired
	private CronEngine cronEngine;
	
	/**
	 * Retorna lista dos jobs ativos.
	 * 
	 * Ex:
	 
$ curl http://localhost:8080/api/jobs
#   [{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}]

	 * @return
	 */
	@RequestMapping(value="/jobs", method=RequestMethod.GET, produces="application/json")
	public List<Job> list() {
		List<Job> jobs = cronEngine.listJobs();
		return jobs;
	}
	
	/**
	 * Cria um novo job para execução. O job deve ser enviado em formato JSON no
	 * corpo do request (POST).
	 * 
	 * Ex:
curl -H "Content-Type: application/json" -X POST \
  -d '{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}' \
  http://localhost:8080/api/jobs
	 * 
	 * @param body
	 */
	@RequestMapping(value="/jobs", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public SimpleResponse create(@RequestBody String body, HttpServletResponse response) {	
		Job job;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			job = objectMapper.readValue(body, Job.class);
			
		} catch (JsonParseException pe) {
			String errMsg = "Malformed JSON in request data: " + pe.getMessage();
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new SimpleResponse(false, errMsg);
			
		} catch (IOException ioe) {
			// A ocorrencia desta excecao nao é esperada. Relança como um erro genérico de runtime.
 			throw new RuntimeException(ioe);
		}
		
		try {
			validate(job);
		} catch (IllegalArgumentException iae) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new SimpleResponse(false, iae.getMessage());
		}
		
		try {
			cronEngine.addJob(job);
		} catch (JobAlreadyExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new SimpleResponse(false, e.getMessage());
		} catch (InvalidCronExpressionException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new SimpleResponse(false, e.getMessage());
		}
		return new SimpleResponse(true, "Job created successfully");
	}
	
	/**
	 * Valida a existencia dos atributos do Job
	 * @param job
	 */
	private void validate(Job job) {
		if (job == null) {
			throw new IllegalArgumentException("Job cannot be null");
		}
		
		if (StringUtils.isBlank(job.getName())) {
			throw new IllegalArgumentException("Missing 'name' attribute in input data");
		}

		if (StringUtils.isBlank(job.getMsg())) {
			throw new IllegalArgumentException("Missing 'msg' attribute in input data");
		}

		if (StringUtils.isBlank(job.getCron())) {
			throw new IllegalArgumentException("Missing 'cron' attribute in input data");
		}
	}

	/**
	 * Cancela/remove um Job. A identificacao do job é enviada na URL
	 * 
	 * Ex:
curl -X DELETE http://localhost:8080/api/jobs/job-name
	 * 
	 * @param body
	 */
	@RequestMapping(value="/jobs/{name}", method=RequestMethod.DELETE, produces="application/json")
	public SimpleResponse delete(@PathVariable("name") String name, HttpServletResponse response) {
		try {
			cronEngine.deleteJob(name);
		} catch (NoSuchJobException e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new SimpleResponse(false, e.getMessage());
		}
		return new SimpleResponse(true, "Job deleted successfully");
	}
	
	/**
	 * 
	 * @param cronEngine
	 */
	public void setCronEngine(CronEngine cronEngine) {
		this.cronEngine = cronEngine;
	}
}
