package links.backend;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class LinksBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinksBackendApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(LinkService repo) {
		return args -> {
		};
	}

}
