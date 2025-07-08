package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import com.cantinasa.cantinasa.model.enums.categoria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.Files;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdicionarProdutoController {
    @FXML private TextField nomeField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeEstoqueField;
    @FXML private TextField estoqueMinimoField;
    @FXML private DatePicker validadePicker;
    @FXML private ComboBox<categoria> categoriaComboBox;
    @FXML private ImageView imagePreview;
    @FXML private TextField descricaoField;
    @FXML private Button saveButton;
    private String imagemUrl = null;
    private volatile boolean uploadingImage = false;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL_PRODUTOS = "http://localhost:8080/api/produtos";

    @FXML
    private void initialize() {
        categoriaComboBox.getItems().setAll(categoria.values());
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Produto");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(nomeField.getScene().getWindow());
        if (file != null) {
            imagePreview.setImage(new Image(file.toURI().toString()));
            uploadingImage = true;
            Platform.runLater(() -> saveButton.setDisable(true));
            new Thread(() -> {
                try {
                    String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                    HttpURLConnection conn = (HttpURLConnection) new java.net.URL("http://localhost:8080/imagens/upload").openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    OutputStream output = conn.getOutputStream();
                    String filePartHeader = "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n" +
                        "Content-Type: " + Files.probeContentType(file.toPath()) + "\r\n\r\n";
                    output.write(filePartHeader.getBytes());
                    try (InputStream input = new FileInputStream(file)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = input.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                    }
                    output.write("\r\n".getBytes());
                    output.write(("--" + boundary + "--\r\n").getBytes());
                    output.flush();
                    output.close();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        String response = new String(conn.getInputStream().readAllBytes());
                        System.out.println("Resposta do upload: " + response);
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            java.util.Map<String, Object> json = mapper.readValue(response, java.util.Map.class);
                            Object urlObj = json.get("url");
                            Platform.runLater(() -> {
                                if (urlObj != null) {
                                    imagemUrl = urlObj.toString();
                                    System.out.println("URL extraída: " + imagemUrl);
                                    saveButton.setDisable(false);
                                } else {
                                    imagemUrl = null;
                                    saveButton.setDisable(true);
                                }
                            });
                        } catch (Exception ex) {
                            Platform.runLater(() -> {
                                imagemUrl = null;
                                saveButton.setDisable(true);
                            });
                        }
                    } else {
                        Platform.runLater(() -> {
                            imagemUrl = null;
                            saveButton.setDisable(true);
                        });
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        imagemUrl = null;
                        saveButton.setDisable(true);
                    });
                } finally {
                    uploadingImage = false;
                }
            }).start();
        }
    }

    @FXML
    private void handleSave() {
        System.out.println("URL enviada no produto: " + imagemUrl);
        if (uploadingImage) {
            showError("Aguarde o upload da imagem", "Por favor, aguarde o envio da imagem antes de salvar.");
            return;
        }
        if (imagemUrl == null || imagemUrl.isBlank()) {
            showError("Imagem obrigatória", "Selecione e envie uma imagem antes de salvar o produto.");
            return;
        }
        if (!validateFields()) return;
        ProdutoDTO produto = new ProdutoDTO();
        produto.setNome(nomeField.getText().trim());
        produto.setDescricao(descricaoField.getText().trim());
        produto.setPreco(Double.parseDouble(precoField.getText().trim()));
        produto.setQuantidade_estoque(Integer.parseInt(quantidadeEstoqueField.getText().trim()));
        produto.setEstoque_minimo(Integer.parseInt(estoqueMinimoField.getText().trim()));
        produto.setValidade(validadePicker.getValue());
        produto.setCategoria(categoriaComboBox.getValue());
        produto.setImagemUrl(imagemUrl);
        java.util.Map<String, Object> produtoRequest = new java.util.HashMap<>();
        produtoRequest.put("userId", 6L);
        produtoRequest.put("password", "admin");
        produtoRequest.put("produto", produto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<java.util.Map<String, Object>> entity = new HttpEntity<>(produtoRequest, headers);
        new Thread(() -> {
            try {
                ResponseEntity<?> response = restTemplate.postForEntity(API_URL_PRODUTOS, entity, Object.class);
                Platform.runLater(() -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        showSuccess("Produto adicionado com sucesso!");
                        close();
                    } else {
                        showError("Erro ao adicionar produto", "Verifique os dados e tente novamente.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Erro ao adicionar produto", e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private boolean validateFields() {
        if (nomeField.getText().isBlank() || precoField.getText().isBlank() || quantidadeEstoqueField.getText().isBlank() || estoqueMinimoField.getText().isBlank() || validadePicker.getValue() == null || categoriaComboBox.getValue() == null) {
            showError("Campos obrigatórios", "Preencha todos os campos do produto.");
            return false;
        }
        try {
            Double.parseDouble(precoField.getText().trim());
            Integer.parseInt(quantidadeEstoqueField.getText().trim());
            Integer.parseInt(estoqueMinimoField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Campos inválidos", "Preço, quantidade e estoque mínimo devem ser numéricos.");
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
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }
} 