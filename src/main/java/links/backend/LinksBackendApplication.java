package links.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
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
	BeanPostProcessor beanPostProcessor() {
		return new BeanPostProcessor() {
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof ObjectMapper mapper) {
					mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
				}
				return bean;
			}
		};
	}

	@Bean
	ApplicationRunner runner(LinkService repo) {
		return args -> {
		};
	}

}
