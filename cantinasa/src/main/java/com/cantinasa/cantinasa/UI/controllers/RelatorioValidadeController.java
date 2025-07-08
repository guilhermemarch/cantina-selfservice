package com.cantinasa.cantinasa.UI.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class RelatorioValidadeController {
    private final RestTemplate restTemplate = new RestTemplate();

    @FXML private TextField diasField;
    @FXML private TableView<Map<String, Object>> validadeTable;
    @FXML private TableColumn<Map<String, Object>, String> nomeColumn;
    @FXML private TableColumn<Map<String, Object>, String> validadeColumn;

    @FXML
    private void handleSearch() {
        String diasStr = diasField.getText();
        int tempDias;
        try { tempDias = Integer.parseInt(diasStr); } catch (Exception ignored) { tempDias = 5; }
        final int dias = tempDias;
        
        new Thread(() -> {
            try {
                String url = "http://localhost:8080/api/relatorios/validade?diasParaVencer=" + dias;
                List result = restTemplate.getForObject(url, List.class);
                Platform.runLater(() -> {
                    ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
                    if (result != null) {
                        for (Object o : result) {
                            data.add((Map<String, Object>) o);
                        }
                    }
                    nomeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty((String) cell.getValue().get("nome")));
                    validadeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("validade"))));
                    validadeTable.setItems(data);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao buscar produtos próximos do vencimento: " + e.getMessage()));
            }
        }).start();
    }
    @FXML
    private void handleClose() {
        Stage stage = (Stage) validadeTable.getScene().getWindow();
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