package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.service.PagamentoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.collections.ObservableList;
import com.cantinasa.cantinasa.model.Item_pedido;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;

@Controller
public class PaymentController {

    @Autowired
    private PagamentoService pagamentoService;

    @FXML
    private RadioButton cashRadio;

    @FXML
    private RadioButton cardRadio;

    @FXML
    private RadioButton pixRadio;

    @FXML
    private VBox cashPaymentSection;

    @FXML
    private VBox cardPaymentSection;

    @FXML
    private VBox pixPaymentSection;

    @FXML
    private TextField cashAmountField;

    @FXML
    private Label changeLabel;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expiryField;

    @FXML
    private TextField cvvField;

    @FXML
    private ImageView pixQrCode;

    @FXML
    private Label totalLabel;

    private Stage stage;
    private double totalAmount;

    public PaymentController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        setupPaymentMethodListeners();
        setupInputValidation();

        ShoppingCartController cartController = MainController.getInstance().getShoppingCartController();
        if (cartController != null) {
            this.totalAmount = cartController.getTotal().doubleValue();
            totalLabel.setText(String.format("R$ %.2f", totalAmount));
        }
    }

    private void setupPaymentMethodListeners() {
        cashRadio.setOnAction(e -> showPaymentSection(cashPaymentSection));
        cardRadio.setOnAction(e -> showPaymentSection(cardPaymentSection));
        pixRadio.setOnAction(e -> showPaymentSection(pixPaymentSection));

        cashAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    double amount = Double.parseDouble(newValue);
                    double change = amount - totalAmount;
                    changeLabel.setText(String.format("Troco: R$ %.2f", change));
                } catch (NumberFormatException ex) {
                    changeLabel.setText("Valor inválido");
                }
            } else {
                changeLabel.setText("");
            }
        });
    }

    private void setupInputValidation() {
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardNumberField.setText(oldValue);
            }
        });

        expiryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}/\\d{0,2}")) {
                expiryField.setText(oldValue);
            }
        });

        cvvField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 3) {
                cvvField.setText(oldValue);
            }
        });
    }

    private void showPaymentSection(VBox section) {
        cashPaymentSection.setVisible(false);
        cardPaymentSection.setVisible(false);
        pixPaymentSection.setVisible(false);
        section.setVisible(true);
    }

    public void setTotalAmount(double amount) {
        this.totalAmount = amount;
        totalLabel.setText(String.format("R$ %.2f", amount));
    }

    @FXML
    private void handleCopyPixCode() {
        String pixCode = "00020126580014BR.GOV.BCB.PIX0136123e4567-e89b-12d3-a456-426614174000";
        try {
            StringSelection selection = new StringSelection(pixCode);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            showAlert("Código PIX copiado", "O código PIX foi copiado para a área de transferência.");
        } catch (java.awt.HeadlessException e) {
            showAlert("Erro ao copiar", "Não é possível copiar o código PIX neste ambiente.");
        }
    }

    @FXML
    private void handleCancel() {
        MainController.getInstance().loadView("shopping-cart");
    }

    @FXML
    private void handleConfirmPayment() {
        if (cashRadio.isSelected()) {
            handleCashPayment();
        } else if (cardRadio.isSelected()) {
            handleCardPayment();
        } else if (pixRadio.isSelected()) {
            handlePixPayment();
        }
    }

    private void handleCashPayment() {
        try {
            double amount = Double.parseDouble(cashAmountField.getText());
            if (amount < totalAmount) {
                showAlert("Valor insuficiente", "O valor recebido é menor que o total a pagar.");
                return;
            }
            processPayment(Pagamento.MetodoPagamento.DINHEIRO);
        } catch (NumberFormatException e) {
            showAlert("Valor inválido", "Por favor, insira um valor válido.");
        }
    }

    private void handleCardPayment() {
        if (cardNumberField.getText().length() != 16) {
            showAlert("Cartão inválido", "O número do cartão deve ter 16 dígitos.");
            return;
        }
        if (!expiryField.getText().matches("\\d{2}/\\d{2}")) {
            showAlert("Data inválida", "A data de validade deve estar no formato MM/AA.");
            return;
        }
        if (cvvField.getText().length() != 3) {
            showAlert("CVV inválido", "O CVV deve ter 3 dígitos.");
            return;
        }
        processPayment(Pagamento.MetodoPagamento.CARTAO);
    }

    private void handlePixPayment() {
        processPayment(Pagamento.MetodoPagamento.PIX);
    }

    private void processPayment(Pagamento.MetodoPagamento method) {
        ShoppingCartController cartController = MainController.getInstance().getShoppingCartController();
        if (cartController == null || cartController.getCartItems().isEmpty()) {
            showAlert("Erro", "Carrinho não encontrado ou vazio.");
            return;
        }

        try {
            // 1. Monta o JSON dos itens
            StringBuilder itensJson = new StringBuilder();
            ObservableList<Item_pedido> cartItems = cartController.getCartItems();
            for (int i = 0; i < cartItems.size(); i++) {
                Item_pedido item = cartItems.get(i);
                itensJson.append(String.format("{\"produtoId\":%d,\"quantidade\":%d}",
                        item.getProduto().getId(), item.getQuantidade()));
                if (i < cartItems.size() - 1) {
                    itensJson.append(",");
                }
            }

            String pagamentoJson = String.format(java.util.Locale.US, "{\"tipoPagamento\":\"%s\",\"valor\":%.2f}",
                    method.name(), totalAmount);

            String pedidoJson = String.format("{"
                            + "\"dataHora\":\"%s\","
                            + "\"status\":\"PENDENTE\","
                            + "\"usuarioId\":1," // TODO: Usar usuário logado
                            + "\"itens\":[%s],"
                            + "\"pagamento\":%s"
                            + "}",
                    java.time.LocalDateTime.now().toString(),
                    itensJson.toString(),
                    pagamentoJson
            );

            // 4. Envia para o endpoint
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(pedidoJson, headers);
            String url = "http://localhost:8080/api/pedidos";
            
            restTemplate.postForEntity(url, entity, String.class);

            showAlert("Pagamento realizado", "O pagamento foi processado com sucesso!");
            MainController.getInstance().loadView("receipt"); // ou uma tela de sucesso

        } catch (Exception e) {
            showAlert("Erro no pagamento", "Ocorreu um erro ao processar o pagamento: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 