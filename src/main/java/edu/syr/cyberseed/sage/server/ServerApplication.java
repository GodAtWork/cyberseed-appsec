package edu.syr.cyberseed.sage.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ImportResource("classpath:app-config.xml")
//@EnableSwagger2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
        logger.info("CyberSeed 2017 SaGe Server Application Startup.");
	}

}
