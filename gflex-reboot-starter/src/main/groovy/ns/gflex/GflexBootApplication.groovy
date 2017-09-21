package ns.gflex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GflexBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GflexBootApplication.class, args);
	}
}
