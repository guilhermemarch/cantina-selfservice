package com.cantinasa.cantinasa.UI;

import com.cantinasa.cantinasa.UI.styles.UIStyles;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class JFrame extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(com.cantinasa.cantinasa.CantinasaApplication.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CantinaSA");
        
        // Apply main container styles
        if (rootNode instanceof VBox) {
            UIStyles.applyMainContainerStyle((VBox) rootNode);
        }
        
        Scene scene = new Scene(rootNode, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        // Set fixed window size
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.setMaxWidth(1024);
        primaryStage.setMaxHeight(768);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
