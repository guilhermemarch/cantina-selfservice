package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.service.PagamentoService;
import com.cantinasa.cantinasa.UI.service.ApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import com.cantinasa.cantinasa.model.Item_pedido;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    private PagamentoService pagamentoService;
    
    @Autowired
    private ApiService apiService;

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

    @FXML
    private Label usernameLabel;

    private Stage stage;
    private double totalAmount;
    private static String currentUsername = null;
    private static Long currentUserId = null;

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static void setCurrentUserId(Long userId) {
        currentUserId = userId;
    }

    public static Long getCurrentUserId() {
        return currentUserId;
    }

    public PaymentController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        setupPaymentMethodListeners();
        setupInputValidation();

        if (usernameLabel != null) {
            if (currentUsername != null && !currentUsername.isBlank()) {
                usernameLabel.setText("Usuário: " + currentUsername);
            } else {
                usernameLabel.setText("Usuário: desconhecido");
            }
        }

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
        cashPaymentSection.setManaged(false);
        cardPaymentSection.setVisible(false);
        cardPaymentSection.setManaged(false);
        pixPaymentSection.setVisible(false);
        pixPaymentSection.setManaged(false);
        section.setVisible(true);
        section.setManaged(true);
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
            showAlert("Código PIX copiado", "O código PIX foi copiado para a área de transferência.");
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
            Map<String, Object> pedidoResult = apiService.criarPedido(
                cartController.getCartItems(), totalAmount, method);
            
            if (!(Boolean) pedidoResult.get("success")) {
                showAlert("Erro ao criar pedido", (String) pedidoResult.get("error"));
                return;
            }
            
            String codigoPix = null;
            Double troco = null;
            
            if (method == Pagamento.MetodoPagamento.PIX) {
                codigoPix = "PIX" + System.currentTimeMillis();
            } else if (method == Pagamento.MetodoPagamento.DINHEIRO) {
                try {
                    double amountReceived = Double.parseDouble(cashAmountField.getText());
                    troco = amountReceived - totalAmount;
                } catch (NumberFormatException e) {
                    troco = 0.0;
                }
            }
            
            Long pedidoId = (Long) pedidoResult.get("pedidoId");
            if (pedidoId == null) {
                showAlert("Erro ao criar pedido", "ID do pedido não retornado pela API.");
                return;
            }
            Map<String, Object> pagamentoResult = apiService.processarPagamento(
                pedidoId, totalAmount, method, codigoPix, troco);
            
            if (!(Boolean) pagamentoResult.get("success")) {
                showAlert("Erro ao processar pagamento", (String) pagamentoResult.get("error"));
                return;
            }

            showAlert("Pagamento realizado", "O pagamento foi processado com sucesso!");
            // Carregar recibo com dados reais
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/receipt.fxml"));
            Parent receiptView = loader.load();
            ReceiptController receiptController = loader.getController();
            String orderNumber = String.valueOf(pedidoId);
            String cliente = (currentUsername != null && !currentUsername.isBlank()) ? currentUsername : "Desconhecido";
            String data = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(java.time.LocalDateTime.now());
            String pagamento = method.name();
            String total = String.format("R$ %.2f", totalAmount);
            String trocoStr = (troco != null && troco > 0.01) ? String.format("R$ %.2f", troco) : "";
            java.util.List<String> itens = new java.util.ArrayList<>();
            for (Item_pedido item : cartController.getCartItems()) {
                itens.add(String.format("- %dx %s - R$ %.2f", item.getQuantidade(), item.getProduto().getNome(), item.getSubtotal()));
            }
            receiptController.preencherRecibo(orderNumber, cliente, data, pagamento, total, trocoStr, itens);
            MainController.getInstance().getContentArea().getChildren().setAll(receiptView);
            MainController.getInstance().setCurrentView("receipt");
            cartController.getCartItems().clear();
            return;

        } catch (Exception e) {
            showAlert("Erro no pagamento", "Ocorreu um erro ao processar o pagamento: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title)  ;
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 