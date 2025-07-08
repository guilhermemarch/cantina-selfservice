package com.cantinasa.cantinasa.UI.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class RelatorioDetalhadoController {
    private final RestTemplate restTemplate = new RestTemplate();

    @FXML private DatePicker salesStartDatePicker;
    @FXML private DatePicker salesEndDatePicker;
    @FXML private TableView<Map.Entry<String, Integer>> salesTable;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> productColumn;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> quantitySoldColumn;
    @FXML private Label salesSummaryLabel;

    @FXML private TextField estoqueBaixoLimiteField;
    @FXML private TableView<Map<String, Object>> estoqueBaixoTable;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoNomeColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoDescricaoColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoPrecoColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoQtdColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoMinColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoValidadeColumn;
    @FXML private TableColumn<Map<String, Object>, String> estoqueBaixoCategoriaColumn;

    @FXML private TextField validadeDiasField;
    @FXML private TableView<Map<String, Object>> validadeTable;
    @FXML private TableColumn<Map<String, Object>, String> validadeNomeColumn;
    @FXML private TableColumn<Map<String, Object>, String> validadeDataColumn;

    @FXML private DatePicker horarioPicoDatePicker;
    @FXML private TableView<Map<String, Object>> horarioPicoTable;
    @FXML private TableColumn<Map<String, Object>, String> horarioPicoHoraColumn;
    @FXML private TableColumn<Map<String, Object>, String> horarioPicoTotalColumn;

    @FXML
    private void handleSalesReport() {
        LocalDate start = salesStartDatePicker.getValue();
        LocalDate end = salesEndDatePicker.getValue();
        if (start == null || end == null) {
            showError("Selecione as datas inicial e final.");
            return;
        }
        salesSummaryLabel.setText("Carregando...");
        new Thread(() -> {
            try {
                String url = String.format("http://localhost:8080/api/relatorios/datas/%s/%s", start.atStartOfDay(), end.atTime(LocalTime.MAX));
                Map result = restTemplate.getForObject(url, Map.class);
                Platform.runLater(() -> {
                    if (result != null) {
                        salesSummaryLabel.setText("Total de vendas: R$ " + result.get("totalSales"));
                        List<Map<String, Object>> topSelling = (List<Map<String, Object>>) result.get("topSellingProducts");
                        ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList();
                        if (topSelling != null) {
                            for (Object o : topSelling) {
                                List entry = (List) o;
                                Map prod = (Map) entry.get(0);
                                String nome = (String) prod.get("nome");
                                Integer qtd = ((Number) entry.get(1)).intValue();
                                data.add(new AbstractMap.SimpleEntry<>(nome, qtd));
                            }
                        }
                        productColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getKey()));
                        quantitySoldColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getValue()).asObject());
                        salesTable.setItems(data);
                    } else {
                        salesSummaryLabel.setText("Nenhum dado encontrado.");
                        salesTable.setItems(FXCollections.observableArrayList());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Erro ao buscar relatório de vendas: " + e.getMessage());
                    salesSummaryLabel.setText("");
                });
            }
        }).start();
    }

    @FXML
    private void handleEstoqueBaixoReport() {
        String limiteStr = estoqueBaixoLimiteField.getText();
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
                    estoqueBaixoNomeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty((String) cell.getValue().get("nome")));
                    estoqueBaixoDescricaoColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty((String) cell.getValue().get("descricao")));
                    estoqueBaixoPrecoColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("preco"))));
                    estoqueBaixoQtdColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("quantidade"))));
                    estoqueBaixoMinColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("estoqueMinimo"))));
                    estoqueBaixoValidadeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("validade"))));
                    estoqueBaixoCategoriaColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("categoria"))));
                    estoqueBaixoTable.setItems(data);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao buscar estoque baixo: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleValidadeReport() {
        String diasStr = validadeDiasField.getText();
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
                    validadeNomeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty((String) cell.getValue().get("nome")));
                    validadeDataColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("validade"))));
                    validadeTable.setItems(data);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao buscar produtos próximos do vencimento: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleHorarioPicoReport() {
        LocalDate data = horarioPicoDatePicker.getValue();
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
                    horarioPicoHoraColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("hora"))));
                    horarioPicoTotalColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().get("total_vendido"))));
                    horarioPicoTable.setItems(dataList);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao buscar horários de pico: " + e.getMessage()));
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