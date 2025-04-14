package com.mangoboss.app.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns(
				"http://localhost:5173",
				"https://dr2f24xo5uydt.cloudfront.net", // 프론트 test
				"https://d3741u3vzg4n3d.cloudfront.net", // 프론트 dev
				"https://dx44qcj8tqeon.cloudfront.net" // 프론트 prod
			)
			.allowedMethods("*")
			.allowedHeaders("*")
			.exposedHeaders("*")
			.allowCredentials(true);
	}
}
