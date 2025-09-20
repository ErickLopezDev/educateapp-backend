package com.lopezcampos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
        .info(new Info()
            .title("EducateApp System API")
            .description("API para examen T1")
            .version("1.0"));
  }
}
