package com.bonrix.dggenraterset.Utility;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.bonrix.dggenraterset.jobs.ListnerJob;



@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableAsync
@ComponentScan(basePackages = "com.bonrix")
@Import({ SecurityConfig.class})
public class Appconfig extends WebMvcConfigurerAdapter{
	
	
	/*@Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable();
	  }*/
	 
		/*@Bean
		public ViewResolver viewResolver() {
			InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
			viewResolver.setViewClass(JstlView.class);
			viewResolver.setPrefix("/WEB-INF/angularview/");
			viewResolver.setSuffix(".jsp");
			return viewResolver;
		}
	/*
		@Override
	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/").setViewName("forward:/login.htm");
	    }*/
		@Bean(name = "multipartResolver")
		public CommonsMultipartResolver resolver() {
			return new CommonsMultipartResolver();
		}
		
		@Override
		public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		     registry.addResourceHandler("/resources/static/js/**")
             .addResourceLocations("/resources/static/js/");
     registry.addResourceHandler("/resources/static/css/**")
             .addResourceLocations("/resources/static/css/");
     registry.addResourceHandler("/resources/static/views/**")
             .addResourceLocations("/resources/static/views/");
     registry.addResourceHandler("/resources/static/**")
             .addResourceLocations("/resources/static/");
     registry.addResourceHandler("/webjars/**")
             .addResourceLocations("/webjars/");
		}
		@Override
	    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	        configurer.enable();
	    }
		  @Bean
		    public MessageSource messageSource() {
		    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		        messageSource.setBasename("application");
		        messageSource.setDefaultEncoding("UTF-8");
		        return messageSource;
		    }
		  
		  @Bean(name="lisjobb")
		  public ListnerJob getlistnerJob()
		  {
			  return new ListnerJob();
		  }

}
