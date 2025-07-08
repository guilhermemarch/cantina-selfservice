package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import com.cantinasa.cantinasa.model.dto.ProdutoRequest;
import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import com.cantinasa.cantinasa.model.enums.categoria;
import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.ProdutoProximoValidadeDTO;
import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.HorarioPicoDTO;
import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.ProdutoEstoqueBaixoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.cantinasa.cantinasa.model.Produto;
import javafx.scene.input.MouseButton;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.Arrays;
import java.util.List;
import com.cantinasa.cantinasa.model.Usuario;
import javafx.scene.control.TableRow;


import java.util.Arrays;
import java.util.List;

@Controller
public class UserChoiceController {


    private final String API_URL_GET_ALL_USERS = "http://localhost:8080/api/usuarios";

    private final RestTemplate restTemplate = new RestTemplate();

    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();



    @FXML
    private void handleCreateAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adicionar-usuario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Adicionar Usuário");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            fetchAllUsers();
        } catch (Exception e) {
            showError("Erro ao abrir tela de adicionar usuário", e.getMessage());
        }
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void fetchAllUsers() {
        javafx.concurrent.Task<List<Usuario>> task = new javafx.concurrent.Task<>() {
            @Override
            protected List<Usuario> call() throws Exception {
                Usuario[] users = restTemplate.getForObject(API_URL_GET_ALL_USERS, Usuario[].class);
                return Arrays.asList(users);
            }
        };
        task.setOnSucceeded(e -> {
            usuarios.setAll(task.getValue());
        });
        task.setOnFailed(e -> {
            showError("Erro ao buscar usuários", task.getException().getMessage());
        });
        new Thread(task).start();
    }



    @FXML
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            showError("Erro ao abrir tela de login", e.getMessage());
        }
    }

    @FXML
    private void handleContinueWithoutAccount() {
        PaymentController.setCurrentUsername("desconhecido");
        PaymentController.setCurrentUserId(0L);
        MainController.getInstance().loadView("payment");
    }

    @FXML
    private void handleBack() {
        MainController.getInstance().loadView("shopping-cart");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 