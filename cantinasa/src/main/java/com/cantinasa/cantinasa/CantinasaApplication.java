package com.cantinasa.cantinasa;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CantinasaApplication {

	private static ConfigurableApplicationContext springContext;

	public static void main(String[] args) {

		springContext = SpringApplication.run(CantinasaApplication.class, args);

		Application.launch(JavaFXApplication.class, args);
	}

	public static ConfigurableApplicationContext getSpringContext() {
		return springContext;
	}

}
