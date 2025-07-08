package com.cantinasa.cantinasa;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Classe principal da aplicação Spring Boot. Responsável por inicializar o
 * contexto do Spring e iniciar a aplicação JavaFX.
 */
@SpringBootApplication
public class CantinasaApplication {

	private static ConfigurableApplicationContext springContext;

        /**
         * Ponto de entrada da aplicação. Inicializa o contexto Spring e
         * delega a execução para a aplicação JavaFX.
         *
         * @param args argumentos de linha de comando
         */
        public static void main(String[] args) {
                springContext = SpringApplication.run(CantinasaApplication.class, args);
                Application.launch(JavaFXApplication.class, args);
        }

        /**
         * Obtém o {@link ConfigurableApplicationContext} criado pelo Spring.
         *
         * @return contexto da aplicação Spring
         */
        public static ConfigurableApplicationContext getSpringContext() {
                return springContext;
        }

}
