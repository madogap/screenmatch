package com.br.mado.ScreenMatchJpa;

import com.br.mado.ScreenMatchJpa.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class ScreenMatchJpaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main();
		main.exibeMenu();
	}
}
