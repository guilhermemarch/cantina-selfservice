package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField senhaField;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL_LOGIN = "http://localhost:8080/api/usuarios/login";

    @FXML
    private void handleLogin() {
        if (!validateFields()) return;

        String username = usernameField.getText().trim();
        String password = senhaField.getText().trim();

        String jsonBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        new Thread(() -> {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(API_URL_LOGIN, entity, String.class);
                Platform.runLater(() -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        Long userId = null;
                        try {
                            String body = response.getBody();
                            if (body != null && body.contains("\"id\"")) {
                                int idIndex = body.indexOf("\"id\"");
                                int colon = body.indexOf(":", idIndex);
                                int comma = body.indexOf(",", colon);
                                String idStr = body.substring(colon + 1, comma > 0 ? comma : body.length()).replaceAll("[^0-9]", "").trim();
                                if (!idStr.isEmpty()) userId = Long.parseLong(idStr);
                            }
                        } catch (Exception ex) { userId = null; }
                        PaymentController.setCurrentUserId(userId);
                        showSuccess("Login realizado com sucesso!");
                        close();
                        PaymentController.setCurrentUsername(usernameField.getText().trim());
                        MainController.getInstance().loadView("payment");
                    } else if (response.getStatusCode().value() == 401) {
                        showError("Usuário ou senha incorretos", "Verifique suas credenciais e tente novamente.");
                    } else {
                        showError("Erro ao fazer login", "Erro inesperado: " + response.getStatusCode());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    String msg = e.getMessage();
                    if (msg != null && (msg.contains("401") || msg.toLowerCase().contains("unauthorized"))) {
                        showError("Usuário ou senha incorretos", "Verifique suas credenciais e tente novamente.");
                    } else {
                        showError("Erro ao fazer login", e.getMessage());
                    }
                });
            }
        }).start();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private boolean validateFields() {
        if (usernameField.getText().isBlank() || senhaField.getText().isBlank()) {
            showError("Campos obrigatórios", "Preencha usuário e senha.");
            return false;
        }
        return true;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void close() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
