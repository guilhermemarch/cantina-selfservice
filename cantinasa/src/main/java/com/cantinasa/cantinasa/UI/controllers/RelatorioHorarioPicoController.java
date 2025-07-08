package com.cantinasa.cantinasa.UI.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;

public class RelatorioHorarioPicoController {
    private final RestTemplate restTemplate = new RestTemplate();

    @FXML private DatePicker datePicker;
    @FXML private TableView<Map<String, Object>> horarioTable;
    @FXML private TableColumn<Map<String, Object>, String> horaColumn;
    @FXML private TableColumn<Map<String, Object>, String> totalColumn;

    @FXML
    private void handleSearch() {
        LocalDate data = datePicker.getValue();
        if (data == null) {
            showError("Selecione a data.");
            return;
        }
        new Thread(() -> {
            try {
                String url = "http://localhost:8080/api/relatorios/horarios-pico?data=" + data;
                List result = restTemplate.getForObject(url, List.class);
                Platform.runLater(() -> {
                    ObservableList<Map<String, Object>> dataList = FXCollections.observableArrayList();
                    if (result != null) {
                        for (Object o : result) {
                            dataList.add((Map<String, Object>) o);
                        }
                    }
                    horaColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("hora"))));
                    totalColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("total_vendido"))));
                    horarioTable.setItems(dataList);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao buscar horários de pico: " + e.getMessage()));
            }
        }).start();
    }
    @FXML
    private void handleClose() {
        Stage stage = (Stage) horarioTable.getScene().getWindow();
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