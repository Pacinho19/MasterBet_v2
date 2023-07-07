package pl.pacinho.MasterBet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MasterBetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasterBetApplication.class, args);
	}

}
