package com.moneyledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyledgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyledgerApplication.class, args);
	}

}
