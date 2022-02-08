package org.hung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolaceClientPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolaceClientPocApplication.class, args);
	}

}
