package com.javatest.scheduleapp.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.javatest.scheduleapp.cron.CronEngine;

/**
 * Listener de application context do spring, destinado a iniciar e parar
 * o CronEngine junto com o start/stop da aplicação.
 * 
 * @author rodrigo
 *
 */
@SuppressWarnings("rawtypes")
@Component
public class ActivationListener implements ApplicationListener {

	@Autowired
	private CronEngine cronEngine;
	
	public void onApplicationEvent(ApplicationEvent evt) {
		
		if (evt instanceof ContextRefreshedEvent) {
			cronEngine.start();
		}
		
		if (evt instanceof ContextClosedEvent) {
			cronEngine.shutdown();
		}
	}


}
