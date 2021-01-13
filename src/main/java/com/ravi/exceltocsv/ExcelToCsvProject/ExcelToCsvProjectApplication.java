package com.ravi.exceltocsv.ExcelToCsvProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * This class will start our application
 * @author RaviP
 *
 */
@SpringBootApplication
public class ExcelToCsvProjectApplication {

	private static final Logger logger=LoggerFactory.getLogger(ExcelToCsvProjectApplication.class);
	
	public static void main(String[] args) {
		logger.info("Application is Starting...");
		SpringApplication.run(ExcelToCsvProjectApplication.class, args);
	}
}
