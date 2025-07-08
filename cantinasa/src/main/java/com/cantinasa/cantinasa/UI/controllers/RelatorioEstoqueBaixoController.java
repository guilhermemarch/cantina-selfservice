package com.cantinasa.cantinasa.UI.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class RelatorioEstoqueBaixoController {
    private final RestTemplate restTemplate = new RestTemplate();

    @FXML private TextField limiteField;
    @FXML private TableView<Map<String, Object>> estoqueTable;
    @FXML private TableColumn<Map<String, Object>, String> nomeColumn;
    @FXML private TableColumn<Map<String, Object>, String> descricaoColumn;
    @FXML private TableColumn<Map<String, Object>, String> precoColumn;
    @FXML private TableColumn<Map<String, Object>, String> quantidadeColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueMinimoColumn;
    @FXML private TableColumn<Map<String, Object>, String> validadeColumn;
    @FXML private TableColumn<Map<String, Object>, String> categoriaColumn;

    @FXML
    private void handleSearch() {
        String limiteStr = limiteField.getText();
        int tempLimite;
        try { tempLimite = Integer.parseInt(limiteStr); } catch (Exception ignored) { tempLimite = 5; }
        final int limite = tempLimite;
        
        new Thread(() -> {
            try {
                String url = "http://localhost:8080/api/relatorios/estoque-baixo?limiteMinimo=" + limite;
                List result = restTemplate.getForObject(url, List.class);
                Platform.runLater(() -> {
                    ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
                    if (result != null) {
                        for (Object o : result) {
                            data.add((Map<String, Object>) o);
                        }
                    }
                    nomeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty((String) cell.getValue().get("nome")));
                    descricaoColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty((String) cell.getValue().get("descricao")));
                    precoColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("preco"))));
                    quantidadeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("quantidade"))));
                    estoqueMinimoColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("estoqueMinimo"))));
                    validadeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("validade"))));
                    categoriaColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("categoria"))));
                    estoqueTable.setItems(data);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao buscar estoque baixo: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) estoqueTable.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

} 