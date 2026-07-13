package com.worksphere.employee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("WorkSphere Employee API")
                        .version("1.0")
                        .description("Employee Management Microservice")
                        .contact(new Contact()
                                .name("Sakshi Agrawal")
                                .email("pinkuagrawal1997@gmail.com")));
    }
}