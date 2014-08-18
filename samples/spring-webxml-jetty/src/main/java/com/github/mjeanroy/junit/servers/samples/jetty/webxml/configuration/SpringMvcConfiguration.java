package com.github.mjeanroy.junit.servers.samples.jetty.webxml.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.github.mjeanroy.junit.servers.samples")
public class SpringMvcConfiguration extends WebMvcConfigurationSupport {

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
		handlerMapping.setAlwaysUseFullPath(true);
		handlerMapping.setUseSuffixPatternMatch(false);
		return handlerMapping;
	}
}
