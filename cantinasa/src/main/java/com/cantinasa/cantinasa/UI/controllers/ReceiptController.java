package com.cantinasa.cantinasa.UI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

@Controller
public class ReceiptController {

    @FXML
    private Label orderNumberLabel;
    @FXML
    private Label clientLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label paymentLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label trocoLabel;
    @FXML
    private VBox itemsBox;

    /**
     * Define o número do pedido que será exibido no recibo.
     * @param orderNumber Número do pedido como string (ex: "12345").
     */
    public void setOrderNumber(String orderNumber) {
        orderNumberLabel.setText("Número do pedido: #" + orderNumber);
    }

    public void preencherRecibo(String orderNumber, String cliente, String data, String pagamento, String total, String troco, java.util.List<String> itens) {
        orderNumberLabel.setText("Pedido: #" + orderNumber);
        clientLabel.setText("Cliente: " + cliente);
        dateLabel.setText("Data: " + data);
        paymentLabel.setText("Pagamento: " + pagamento);
        totalLabel.setText("Total: " + total);
        trocoLabel.setText(troco != null && !troco.isEmpty() ? ("Troco: " + troco) : "");
        itemsBox.getChildren().clear();
        for (String item : itens) {
            Label itemLabel = new Label(item);
            itemLabel.getStyleClass().add("receipt-item-line");
            itemsBox.getChildren().add(itemLabel);
        }
    }

    /**
     * Evento acionado ao clicar em "Voltar ao Início".
     * Retorna o usuário à tela de boas-vindas.
     */
    @FXML
    private void handleBackToHome() {
        MainController.getInstance().loadView("welcome");
    }
}
