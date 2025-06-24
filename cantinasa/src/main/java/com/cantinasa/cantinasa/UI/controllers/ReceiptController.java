package com.cantinasa.cantinasa.UI.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class ReceiptController {
    @FXML
    private Label orderNumberLabel;

    public void setOrderNumber(String orderNumber) {
        orderNumberLabel.setText("Número do pedido: #" + orderNumber);
    }

    @FXML
    private void handleBackToHome() {
        MainController.getInstance().loadView("welcome");
    }
}