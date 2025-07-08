package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.CantinasaApplication;
import com.cantinasa.cantinasa.UI.components.UIComponents;
import com.cantinasa.cantinasa.UI.config.UIConfig;
import com.cantinasa.cantinasa.model.Produto;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

@Controller
public class MainController {

    private static MainController instance;
    private String currentView;
    private ShoppingCartController shoppingCartController;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button adminButton;

    @FXML
    private BorderPane mainBorderPane;

    private HBox headerBox;

    public static MainController getInstance() {
        return instance;
    }

    public ShoppingCartController getShoppingCartController() {
        return shoppingCartController;
    }

    public void setShoppingCartController(ShoppingCartController controller) {
        this.shoppingCartController = controller;
    }

    public StackPane getContentArea() {
        return contentArea;
    }
    public void setCurrentView(String viewName) {
        this.currentView = viewName;
    }

    @FXML
    public void initialize() {
        instance = this;

        headerBox = new HBox();
        headerBox.getStyleClass().add("header");
        headerBox.setStyle("-fx-background-color: transparent;");
        headerBox.setPrefWidth(729);
        headerBox.setVisible(false);
        Label headerTitle = new Label("");
        headerTitle.getStyleClass().add("header-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        headerBox.getChildren().addAll(headerTitle, spacer);
        ((StackPane) mainBorderPane.getParent()).getChildren().add(1, headerBox);

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

        if (headerBox != null) headerBox.setVisible(false);

        if ("admin-login".equals(viewName)) {
            if (mainBorderPane.getScene() != null) {
                Stage stage = (Stage) mainBorderPane.getScene().getWindow();
                stage.setMinWidth(UIConfig.getMinWindowWidth());
                stage.setMinHeight(UIConfig.getMinWindowHeight());
                stage.setMaxWidth(Double.MAX_VALUE);
                stage.setMaxHeight(Double.MAX_VALUE);
                stage.setResizable(true);
            }
        } else if ("admin-dashboard".equals(viewName)) {
            if (mainBorderPane.getScene() != null) {
                Stage stage = (Stage) mainBorderPane.getScene().getWindow();
                stage.setMaximized(true);
            }
        } else {
            if (mainBorderPane.getScene() != null) {
                Stage stage = (Stage) mainBorderPane.getScene().getWindow();
                int width = UIConfig.getDefaultWindowWidth();
                int height = UIConfig.getDefaultWindowHeight();
                stage.setMinWidth(width);
                stage.setMinHeight(height);
                stage.setMaxWidth(width);
                stage.setMaxHeight(height);
                stage.setResizable(false);
            }
        }

        try {
            VBox loadingIndicator = UIComponents.createLoadingIndicator();
            contentArea.getChildren().add(loadingIndicator);

            new Thread(() -> {
                try {
                    URL fxmlUrl = getClass().getResource("/fxml/" + viewName + ".fxml");
                    if (fxmlUrl == null) {
                        throw new IllegalStateException("FXML não encontrado: /fxml/" + viewName + ".fxml");
                    }
                    FXMLLoader loader = new FXMLLoader(fxmlUrl);
                    loader.setControllerFactory(CantinasaApplication.getSpringContext()::getBean);
                    Parent view = loader.load();

                    if ("shopping-cart".equals(viewName)) {
                        shoppingCartController = loader.getController();
                    }

                    javafx.application.Platform.runLater(() -> {
                        if (headerBox != null) headerBox.setVisible(false);
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

                        if (!"admin-login".equals(viewName) && headerBox != null) {
                            headerBox.setVisible(true);
                        } else if (headerBox != null) {
                            headerBox.setVisible(false);
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
