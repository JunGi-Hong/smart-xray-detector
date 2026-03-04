package dna.safe_guard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SafeGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeGuardApplication.class, args);
	}

}
