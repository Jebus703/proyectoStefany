package com.saas.workspaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkspacesApplication {

	private static final Logger logger = LoggerFactory.getLogger(WorkspacesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WorkspacesApplication.class, args);
		logger.info(" 🚀Aplicación iniciada correctamente");
	}

}
