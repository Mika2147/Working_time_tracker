package com.mikaauer.wtmauthorizationserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WtmAuthorizationServerApplication {
	private static final Logger LOG = LoggerFactory.getLogger(WtmAuthorizationServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WtmAuthorizationServerApplication.class, args);
	}

}
