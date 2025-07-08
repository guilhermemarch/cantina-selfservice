package com.cantinasa.cantinasa.UI.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.apache.tomcat.util.IntrospectionUtils.capitalize;

public class RelatorioVendasController {
    private final RestTemplate restTemplate = new RestTemplate();

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<Map.Entry<String, Integer>> salesTable;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> productColumn;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> quantitySoldColumn;
    @FXML private Label summaryLabel;

    @FXML
    private void handleSearch() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start == null || end == null) {
            showError("Selecione as datas inicial e final.");
            return;
        }

        summaryLabel.setText("Carregando...");
        salesTable.setItems(FXCollections.observableArrayList());

        new Thread(() -> {
            try {
                String url = String.format(
                        "http://localhost:8080/api/relatorios/datas/%s/%s",
                        start.atStartOfDay(), end.atTime(LocalTime.MAX)
                );

                Map<String, Object> result = restTemplate.getForObject(url, Map.class);

                Platform.runLater(() -> {
                    if (result == null) {
                        summaryLabel.setText("Nenhum dado encontrado.");
                        return;
                    }

                    Number total = (Number) result.get("totalSales");
                    String totalFormatted = String.format("R$ %.2f", total.doubleValue()).replace(".", ",");

                    Map<String, Number> paymentMap = (Map<String, Number>) result.get("salesByPaymentMethod");
                    StringBuilder paymentSummary = new StringBuilder();
                    if (paymentMap != null && !paymentMap.isEmpty()) {
                        for (Map.Entry<String, Number> entry : paymentMap.entrySet()) {
                            String metodo = capitalize(entry.getKey().toLowerCase());
                            String valor = String.format("R$ %.2f", entry.getValue().doubleValue()).replace(".", ",");
                            paymentSummary.append(metodo).append(": ").append(valor).append(" | ");
                        }
                        if (paymentSummary.length() >= 3) {
                            paymentSummary.setLength(paymentSummary.length() - 3);
                        }
                    }

                    summaryLabel.setText("Total de vendas: " + totalFormatted +
                            (paymentSummary.length() > 0 ? "\nPor método: " + paymentSummary.toString() : ""));

                    List<Map<String, Object>> topSelling = (List<Map<String, Object>>) result.get("topSellingProducts");
                    ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList();

                    if (topSelling != null) {
                        for (Map<String, Object> item : topSelling) {
                            String nome = (String) item.get("nome");
                            Integer qtd = ((Number) item.get("quantidadeVendida")).intValue();
                            data.add(new AbstractMap.SimpleEntry<>(nome, qtd));
                        }
                    }

                    productColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
                    quantitySoldColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getValue()).asObject());
                    salesTable.setItems(data);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Erro ao buscar relatório de vendas: " + e.getMessage());
                    summaryLabel.setText("");
                });
            }
        }).start();
    }



    @FXML
    private void handleClose() {
        Stage stage = (Stage) salesTable.getScene().getWindow();
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