package com.musdon.academybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Musdon Bank APIs",
				description = "Backend REST API",
				version = "v1.0",
				contact = @Contact(
						name = "Prashant",
						email = "9prashantverma@gmail.com",
						url = "github.com/pv-912"
				),
				license = @License(
						name = "Musdon back",
						url = "github.com"
				)
			),
		externalDocs = @ExternalDocumentation(
				description = "Musdon bank",
				url = "guthub.com"
				)
		)

public class JavaAcademyBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaAcademyBankApplication.class, args);
	}

}
