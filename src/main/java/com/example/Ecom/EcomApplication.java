package com.example.Ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class EcomApplication {

	public static void main(String[] args) {

		var context=SpringApplication.run(EcomApplication.class, args);
		DataSource ds=context.getBean(DataSource.class);
	}

}
