package com.cantinasa.cantinasa.UI.controllers;

import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

@Controller
public class WelcomeController {

    @FXML
    private void handleContinue() {
        MainController.getInstance().loadView("product-selection");
    }

    @FXML
    private void handleAdminLogin() {
        MainController.getInstance().loadView("admin-login");
    }
} 