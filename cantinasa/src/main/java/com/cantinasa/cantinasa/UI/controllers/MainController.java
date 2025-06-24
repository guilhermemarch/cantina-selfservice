package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.CantinasaApplication;
import com.cantinasa.cantinasa.UI.components.UIComponents;
import com.cantinasa.cantinasa.model.Produto;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MainController {

    private static MainController instance;
    private String currentView;
    private ShoppingCartController shoppingCartController;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button adminButton;

    public static MainController getInstance() {
        return instance;
    }

    public ShoppingCartController getShoppingCartController() {
        return shoppingCartController;
    }

    public void setShoppingCartController(ShoppingCartController controller) {
        this.shoppingCartController = controller;
    }

    @FXML
    public void initialize() {
        instance = this;

        loadView("shopping-cart");
        loadView("welcome");
    }

    @FXML
    private void handleAdminLogin() {
        loadView("admin-login");
    }

    public void loadView(String viewName) {
        if (viewName.equals(currentView)) {
            return;
        }

        try {
            VBox loadingIndicator = UIComponents.createLoadingIndicator();
            contentArea.getChildren().add(loadingIndicator);

            new Thread(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + viewName + ".fxml"));
                    loader.setControllerFactory(CantinasaApplication.getSpringContext()::getBean);
                    Parent view = loader.load();

                    if ("shopping-cart".equals(viewName)) {
                        shoppingCartController = loader.getController();
                    }

                    javafx.application.Platform.runLater(() -> {
                        contentArea.getChildren().remove(loadingIndicator);

                        if (!contentArea.getChildren().isEmpty()) {
                            Node currentContent = contentArea.getChildren().get(0);
                            if (currentContent instanceof Parent) {
                                Parent parentContent = (Parent) currentContent;
                                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), parentContent);
                                fadeOut.setFromValue(1.0);
                                fadeOut.setToValue(0.0);
                                fadeOut.setOnFinished(e -> {
                                    contentArea.getChildren().clear();
                                    view.setOpacity(0);
                                    contentArea.getChildren().add(view);
                                    FadeTransition fadeIn = new FadeTransition(Duration.millis(200), view);
                                    fadeIn.setFromValue(0.0);
                                    fadeIn.setToValue(1.0);
                                    fadeIn.play();
                                });
                                fadeOut.play();
                            }
                        } else {
                            view.setOpacity(0);
                            contentArea.getChildren().add(view);
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), view);
                            fadeIn.setFromValue(0.0);
                            fadeIn.setToValue(1.0);
                            fadeIn.play();
                        }

                        currentView = viewName;
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() -> {
                        contentArea.getChildren().remove(loadingIndicator);
                        UIComponents.showNotification(contentArea, "Erro ao carregar a tela: " + e.getMessage());
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            UIComponents.showNotification(contentArea, "Erro ao carregar a tela: " + e.getMessage());
        }
    }
}
