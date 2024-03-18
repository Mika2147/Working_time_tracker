package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Database.TimeMeasurementDatabaseConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Configuration
@ComponentScan("com.mikaauer.workingtimemeasurement")
public class WorkingTimeMeasurementApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkingTimeMeasurementApplication.class, args);
	}
}