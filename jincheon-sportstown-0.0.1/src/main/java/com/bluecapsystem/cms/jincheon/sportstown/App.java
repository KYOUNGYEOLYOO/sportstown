package com.bluecapsystem.cms.jincheon.sportstown;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@RestControllerAdvice
@Configuration
// @ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:spring/cms-app-root-context.xml")
@EntityScan(basePackages={"com.bluecapsystem.cms.core.data.entity", "com.bluecapsystem.cms.jincheon.sportstown.data.entity"})
@SpringBootApplication
public class App extends SpringBootServletInitializer
{
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }
 
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
