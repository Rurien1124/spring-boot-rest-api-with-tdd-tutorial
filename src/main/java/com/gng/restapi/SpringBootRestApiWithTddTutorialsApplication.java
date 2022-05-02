package com.gng.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@EnableConfigurationProperties
@EnableEncryptableProperties
@SpringBootApplication
public class SpringBootRestApiWithTddTutorialsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApiWithTddTutorialsApplication.class, args);
	}

}
