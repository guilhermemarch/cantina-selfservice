package com.cantinasa.cantinasa.UI.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

@Controller
public class WelcomeController {

    @FXML private ImageView imageView;
    @FXML private StackPane rootPane;

    @FXML
    private void handleContinue() {
        MainController.getInstance().loadView("product-selection");
    }

    @FXML
    private void handleAdminLogin() {
        MainController.getInstance().loadView("admin-login");
    }

    @FXML
    public void initialize() {
        if (imageView != null && rootPane != null) {
            imageView.fitWidthProperty().bind(rootPane.widthProperty());
            imageView.fitHeightProperty().bind(rootPane.heightProperty());
        }
    }
} 