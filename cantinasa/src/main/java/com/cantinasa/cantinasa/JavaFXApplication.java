package com.cantinasa.cantinasa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import com.cantinasa.cantinasa.UI.config.UIConfig;

/**
 * Inicializa a interface JavaFX da aplicação consumindo o contexto Spring
 * previamente criado. Responsável por carregar o FXML principal e aplicar os
 * estilos.
 */
public class JavaFXApplication extends Application {

    /**
     * Configura e exibe a janela principal da aplicação.
     *
     * @param primaryStage palco principal fornecido pelo JavaFX
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ConfigurableApplicationContext context = CantinasaApplication.getSpringContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
        
        Parent root = loader.load();
        int width = UIConfig.getDefaultWindowWidth();
        int height = UIConfig.getDefaultWindowHeight();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/receipt.css").toExternalForm());


        primaryStage.setTitle("Cantina da Uri - Sistema de Autoatendimento");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        primaryStage.show();
    }

    /**
     * Fecha o contexto Spring ao encerrar a aplicação JavaFX.
     */
    @Override
    public void stop() {
        ConfigurableApplicationContext context = CantinasaApplication.getSpringContext();
        context.close();
    }}