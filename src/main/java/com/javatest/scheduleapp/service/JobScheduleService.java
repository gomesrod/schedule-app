package com.javatest.scheduleapp.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javatest.scheduleapp.model.Job;

@Controller
public class JobScheduleService {

	@RequestMapping(value="/jobs", produces="application/json")
	@ResponseBody
	public Job[] listJobs() {
		return new Job[] {
				new Job("ls", "mensagem", "* * * *"),
				new Job("du", "msg2", "* 10 20 *")
		};
	}
}
