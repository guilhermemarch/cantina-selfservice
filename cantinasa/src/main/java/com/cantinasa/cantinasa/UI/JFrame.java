package com.cantinasa.cantinasa.UI;

import com.cantinasa.cantinasa.UI.config.UIConfig;
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
        
        if (rootNode instanceof VBox) {
            UIStyles.applyMainContainerStyle((VBox) rootNode);
        }
        
        int width = UIConfig.getDefaultWindowWidth();
        int height = UIConfig.getDefaultWindowHeight();
        Scene scene = new Scene(rootNode, width, height);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        
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
