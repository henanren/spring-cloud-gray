package cn.springcloud.gray.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayServerMarkerConfiguration {

	@Bean
	public GrayServerMarker grayServerMarkerBean() {
		System.out.println(123);
		return new GrayServerMarker();
	}

	class GrayServerMarker {
	}
}
