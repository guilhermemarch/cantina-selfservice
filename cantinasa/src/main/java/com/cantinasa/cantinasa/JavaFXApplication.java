package com.cantinasa.cantinasa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConfigurableApplicationContext context = CantinasaApplication.getSpringContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        primaryStage.setTitle("Cantina - Sistema de Autoatendimento");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void stop() {
        ConfigurableApplicationContext context = CantinasaApplication.getSpringContext();
        context.close();
    }
} 
