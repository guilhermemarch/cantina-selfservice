package com.cantinasa.cantinasa.UI.controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.cantinasa.cantinasa.UI.service.ApiService;

public class ProdutoDetalheController {
    @FXML private Label idLabel;
    @FXML private Label nomeLabel;
    @FXML private Label precoLabel;
    @FXML private Label quantidadeLabel;
    @FXML private Label estoqueMinimoLabel;
    @FXML private Label validadeLabel;
    @FXML private Label categoriaLabel;
    @FXML private TextField estoqueField;

    private ProdutoDTO produto;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api/produtos";
    private Long adminUserId;
    private String adminPassword;

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
        idLabel.setText(String.valueOf(produto.getIdProduto()));
        nomeLabel.setText(produto.getNome());
        precoLabel.setText(String.valueOf(produto.getPreco()));
        quantidadeLabel.setText(String.valueOf(produto.getQuantidade_estoque()));
        estoqueMinimoLabel.setText(String.valueOf(produto.getEstoque_minimo()));
        validadeLabel.setText(produto.getValidade() != null ? produto.getValidade().toString() : "");
        categoriaLabel.setText(produto.getCategoria() != null ? produto.getCategoria().name() : "");
    }

    public void setAdminCredentials(Long userId, String password) {
        this.adminUserId = userId;
        this.adminPassword = password;
    }

    @FXML
    private void handleAtualizarEstoque() {
        String adminPassword = "admin";
        int adminUserId = 6;
        String valor = estoqueField.getText();
        int quantidadeDelta;
        try {
            quantidadeDelta = Integer.parseInt(valor);
        } catch (Exception e) {
            showError("Informe um valor numérico para o estoque.");
            return;
        }
        new Thread(() -> {
            try {
                String getUrl = BASE_URL + "/" + produto.getIdProduto();
                java.net.URL url = new java.net.URL(getUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.connect();
                int code = conn.getResponseCode();
                if (code != 200) throw new RuntimeException("Erro ao buscar produto: " + code);
                java.io.InputStream is = conn.getInputStream();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                String responseBody = s.hasNext() ? s.next() : "";
                s.close();
                conn.disconnect();
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                com.cantinasa.cantinasa.model.dto.ProdutoDTO produtoDTO = mapper.readValue(responseBody, com.cantinasa.cantinasa.model.dto.ProdutoDTO.class);
                produtoDTO.setIdProduto(produto.getIdProduto());
                int novaQuantidade = produtoDTO.getQuantidade_estoque() + quantidadeDelta;
                if (novaQuantidade < 0) novaQuantidade = 0;
                produtoDTO.setQuantidade_estoque(novaQuantidade);
                com.cantinasa.cantinasa.model.dto.ProdutoRequest req = new com.cantinasa.cantinasa.model.dto.ProdutoRequest();
                req.setUserId((long)adminUserId);
                req.setPassword(adminPassword);
                req.setProduto(produtoDTO);
                String putBody = mapper.writeValueAsString(req);
                String putUrl = BASE_URL + "/" + produto.getIdProduto();
                java.net.URL urlPut = new java.net.URL(putUrl);
                java.net.HttpURLConnection connPut = (java.net.HttpURLConnection) urlPut.openConnection();
                connPut.setRequestMethod("PUT");
                connPut.setDoOutput(true);
                connPut.setRequestProperty("Content-Type", "application/json");
                try (java.io.OutputStream os = connPut.getOutputStream()) {
                    byte[] input = putBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                int putCode = connPut.getResponseCode();
                java.io.InputStream isPut = (putCode >= 200 && putCode < 400) ? connPut.getInputStream() : connPut.getErrorStream();
                java.util.Scanner sPut = new java.util.Scanner(isPut).useDelimiter("\\A");
                String putResp = sPut.hasNext() ? sPut.next() : "";
                sPut.close();
                connPut.disconnect();
                if (putCode >= 200 && putCode < 300) {
                    Platform.runLater(() -> {
                        showSuccess("Estoque atualizado!");
                        close();
                    });
                } else {
                    Platform.runLater(() -> showError("Erro ao atualizar estoque: " + putResp));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao atualizar estoque: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleRemoverProduto() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja remover este produto?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Remover Produto");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                new Thread(() -> {
                    try {
                        String url = BASE_URL + "/" + produto.getIdProduto();
                        restTemplate.delete(url);
                        Platform.runLater(() -> {
                            showSuccess("Produto removido!");
                            close();
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> showError("Erro ao remover produto: " + e.getMessage()));
                    }
                }).start();
            }
        });
    }

    @FXML
    private void handleClose() {
        close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void close() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }
} 