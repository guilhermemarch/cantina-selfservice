package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AdicionarUsuarioController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL_USUARIOS = "http://localhost:8080/api/usuarios/cadastrar";

    @FXML
    private void handleSave() {
        if (!validateFields()) return;
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        usuario.setUsername(usernameField.getText().trim());
        usuario.setEmail(emailField.getText().trim());
        usuario.setPassword(senhaField.getText().trim());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsuarioRequestDTO> entity = new HttpEntity<>(usuario, headers);
        new Thread(() -> {
            try {
                ResponseEntity<?> response = restTemplate.postForEntity(API_URL_USUARIOS, entity, Object.class);
                Platform.runLater(() -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        showSuccess("Usuário cadastrado com sucesso!");
                        close();
                    } else {
                        showError("Erro ao cadastrar usuário", "Verifique os dados e tente novamente.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao cadastrar usuário", e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private boolean validateFields() {
        if (usernameField.getText().isBlank() || emailField.getText().isBlank() || senhaField.getText().isBlank()) {
            showError("Campos obrigatórios", "Preencha todos os campos do usuário.");
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