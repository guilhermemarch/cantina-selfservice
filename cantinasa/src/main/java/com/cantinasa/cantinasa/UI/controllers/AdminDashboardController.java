package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import com.cantinasa.cantinasa.model.dto.ProdutoRequest;
import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import com.cantinasa.cantinasa.model.enums.categoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class AdminDashboardController {

    private static final String API_URL = "http://localhost:8080/api/produtos";
    private final RestTemplate restTemplate = new RestTemplate();


    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;

    @FXML
    private TextField nomeField;
    
    @FXML
    private TextField precoField;
    
    @FXML
    private TextField quantidadeEstoqueField;
    
    @FXML
    private TextField estoqueMinimoField;
    
    @FXML
    private DatePicker validadePicker;
    
    @FXML
    private ComboBox<categoria> categoriaComboBox;

    @FXML
    private void initialize() {
        categoriaComboBox.getItems().addAll(categoria.values());
    }

    @FXML
    private void handleLogout() {
        MainController.getInstance().loadView("admin-login");
    }

    @FXML
    private void handleBack() {
        MainController.getInstance().loadView("welcome");
    }

    @FXML
    private void handleAddProduct() {
        try {
            ProdutoRequest request = new ProdutoRequest();
            request.setUserId(1L);
            request.setPassword("admin123");
            ProdutoDTO produto = new ProdutoDTO();
            produto.setNome(nomeField.getText());
            produto.setPreco(Double.parseDouble(precoField.getText()));
            produto.setQuantidade_estoque(Integer.parseInt(quantidadeEstoqueField.getText()));
            produto.setEstoque_minimo(Integer.parseInt(estoqueMinimoField.getText()));
            produto.setValidade(validadePicker.getValue());
            produto.setCategoria(categoriaComboBox.getValue());

            request.setProduto(produto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ProdutoRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<?> response = restTemplate.postForEntity(API_URL, entity, Object.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                showSuccess("Produto adicionado com sucesso!");
                clearFields();
            } else {
                showError("Erro ao adicionar produto", "Verifique os dados e tente novamente.");
            }
        } catch (Exception e) {
            showError("Erro ao adicionar produto", e.getMessage());
        }
    }

    private void clearFields() {
        nomeField.clear();
        precoField.clear();
        quantidadeEstoqueField.clear();
        estoqueMinimoField.clear();
        validadePicker.setValue(null);
        categoriaComboBox.setValue(null);
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

    @FXML
    private void handleAddUser(ActionEvent actionEvent) {
        try {
            UsuarioRequestDTO usuario = new UsuarioRequestDTO();
            usuario.setUsername(usernameField.getText());
            usuario.setEmail(emailField.getText());
            usuario.setPassword(senhaField.getText());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioRequestDTO> entity = new HttpEntity<>(usuario, headers);
            String userApiUrl = "http://localhost:8080/api/usuarios/cadastrar";

            ResponseEntity<?> response = restTemplate.postForEntity(userApiUrl, entity, Object.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                showSuccess("Usuário cadastrado com sucesso!");
                clearUserFields();
            } else {
                showError("Erro ao cadastrar", "Verifique os dados e tente novamente.");
            }
        } catch (Exception e) {
            showError("Erro ao cadastrar usuário", e.getMessage());
        }
    }

    @FXML
    private void handleGenerateReport() {
        showFeatureInDevelopment("Gerar Relatório");
    }

    private void clearUserFields() {
        usernameField.clear();
        emailField.clear();
        senhaField.clear();
    }

    private void showFeatureInDevelopment(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Funcionalidade em Desenvolvimento");
        alert.setContentText("A funcionalidade de " + title.toLowerCase() + " será implementada em breve.");
        alert.showAndWait();
    }


}
