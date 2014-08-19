package com.github.mjeanroy.junit.servers.samples.tomcat.java.configuration;

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
		AnnotationConfigWebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context));
		return context;
	}

	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(configLocation());
		return context;
	}

	private void initSpringMvc(ServletContext servletContext, AnnotationConfigWebApplicationContext context) {
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		dispatcherServlet.setContextConfigLocation(configLocation());
		dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring", dispatcherServlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/*");
		dispatcher.addMapping("/");
	}

	private String configLocation() {
		return SpringMvcConfiguration.class.getPackage().getName();
	}
}
