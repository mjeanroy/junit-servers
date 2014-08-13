package com.github.mjeanoy.junit.servers.samples.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebMvc
@ComponentScan("com.github.mjeanroy")
public class SpringMvcConfiguration extends WebMvcConfigurationSupport {

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
		handlerMapping.setAlwaysUseFullPath(true);
		handlerMapping.setUseSuffixPatternMatch(false);
		return handlerMapping;
	}
}
