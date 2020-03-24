package com.kodgemisi.InternshipProject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/joblisting").setViewName("job_listing");
		registry.addViewController("/alljobs").setViewName("all_jobs");
		registry.addViewController("/user-joblisting").setViewName("user-job_listing");
		registry.addViewController("/user-alljobs").setViewName("user-all_jobs");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/apply").setViewName("apply");
	}

}