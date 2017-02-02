package com.javatest.scheduleapp.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javatest.scheduleapp.cron.CronEngine;
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
		try {
			cronEngine.addJob(new Job("job111", "A message", "* * * * * "));
		} catch (JobAlreadyExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new SimpleResponse(false, e.getMessage());
		}
		return new SimpleResponse(true, "Job created successfully");
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
		return new SimpleResponse(true, "Job deleted successfuly");
	}
}
