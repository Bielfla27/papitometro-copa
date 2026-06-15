package copa.papitometroCopaDoMundo;

import java.io.IOException;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PapitometroCopaDoMundoApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(PapitometroCopaDoMundoApplication.class, args);
		
	}

}
