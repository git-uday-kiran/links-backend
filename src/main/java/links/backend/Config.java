package links.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class Config {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		return objectMapper;
	}

	@Bean
	public OpenApiCustomizer openApiCustomizer(ObjectMapper objectMapper) {
		return openApi -> {
			var values = openApi.getComponents().getSchemas().values();
			values.forEach(schema -> {
				var properties = ((Schema<?>) schema).getProperties();
				if (properties != null) {
					var added = new HashMap<String, Schema<?>>();
					properties.forEach((key, value) -> {
						String snakeCaseKey = objectMapper.getPropertyNamingStrategy().nameForField(null, null, key);
						added.put(snakeCaseKey, value);
					});
					properties.clear();
					properties.putAll(added);
				}
			});
		};
	}

}