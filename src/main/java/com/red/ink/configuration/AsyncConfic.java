/**
 * 
 */
package com.red.ink.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ajith
 *
 */

@Configuration 
@EnableAsync
public class AsyncConfic {

	@Bean(name = "taskExicuter")

	public Executor  taskExecutor() {
		
		ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("userThread-");
		executor.initialize();
		return executor;
		
	}

}
