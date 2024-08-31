package com.sabado.filme.repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages ="com.sabado.filme.model")
public class FilmeApplication {
//Comentario
	public static void main(String[] args) {
		SpringApplication.run(FilmeApplication.class, args);
	}

}
