package com.github.mjeanoy.junit.servers.samples.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebApplicationConfiguration implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		initSpring(servletContext);
	}

	private void initSpring(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext context = initContext(servletContext);
		initSpringMvc(servletContext, context);
	}

	private AnnotationConfigWebApplicationContext initContext(ServletContext servletContext) {
		String configLocation = SpringConfiguration.class.getName();
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(configLocation);

		servletContext.addListener(new ContextLoaderListener(context));
		return context;
	}

	private void initSpringMvc(ServletContext servletContext, AnnotationConfigWebApplicationContext context) {
		DispatcherServlet servlet = new DispatcherServlet(context);
		servlet.setContextConfigLocation(SpringMvcConfiguration.class.getName());
		servlet.setContextClass(AnnotationConfigWebApplicationContext.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring", servlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/*");
		dispatcher.addMapping("/");
	}
}
