package com.cantinasa.cantinasa.UI.controllers;

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
import com.cantinasa.cantinasa.UI.config.UIConfig;

@Controller
public class AdminDashboardController {

    private static final String API_URL_PRODUTOS = "http://localhost:8080/api/produtos";
    private static final String API_URL_USUARIOS = "http://localhost:8080/api/usuarios/cadastrar";
    private final String API_URL_GET_ALL_USERS = "http://localhost:8080/api/usuarios";

    private final RestTemplate restTemplate = new RestTemplate();

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField senhaField;

    @FXML private TextField nomeField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeEstoqueField;
    @FXML private TextField estoqueMinimoField;
    @FXML private DatePicker validadePicker;
    @FXML private ComboBox<categoria> categoriaComboBox;

    @FXML private TableView<ProdutoDTO> stockTable;
    @FXML private TableColumn<ProdutoDTO, String> productIdColumn;
    @FXML private TableColumn<ProdutoDTO, String> productNameColumn;
    @FXML private TableColumn<ProdutoDTO, Integer> quantityColumn;
    @FXML private TableColumn<ProdutoDTO, Double> priceColumn;
    @FXML private TableColumn<ProdutoDTO, Integer> minStockColumn;
    @FXML private TableColumn<ProdutoDTO, String> expiryDateColumn;
    @FXML private TableColumn<ProdutoDTO, String> categoryColumn;
    @FXML private TableColumn<ProdutoDTO, Void> actionsColumn;

    @FXML private TableView<Usuario> userTable;
    @FXML private TableColumn<Usuario, String> usernameColumn;
    @FXML private TableColumn<Usuario, String> nameColumn;
    @FXML private TableColumn<Usuario, String> roleColumn;
    @FXML private TableColumn<Usuario, Void> userActionsColumn;
    @FXML private TextField userSearchField;
    @FXML private Button userRefreshButton;
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    @FXML private TextField productSearchField;
    @FXML private TextField productIdSearchField;

    private ObservableList<ProdutoDTO> produtos = FXCollections.observableArrayList();

    private final javafx.concurrent.Task<Void> loadingTask = new javafx.concurrent.Task<>() { public Void call() { return null; } };

    private Long adminUserId;
    private String adminPassword;

    @FXML
    private void initialize() {
        categoriaComboBox.getItems().addAll(categoria.values());
        
        if (productIdColumn != null) productIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdProduto() != null ? cell.getValue().getIdProduto().toString() : ""));
        if (productNameColumn != null) productNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
        if (quantityColumn != null) quantityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantidade_estoque()).asObject());
        if (priceColumn != null) priceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPreco() != null ? cell.getValue().getPreco() : 0.0).asObject());
        if (minStockColumn != null) minStockColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getEstoque_minimo()).asObject());
        if (expiryDateColumn != null) expiryDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValidade() != null ? cell.getValue().getValidade().toString() : ""));
        if (categoryColumn != null) categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria() != null ? cell.getValue().getCategoria().name() : ""));
        
        if (usernameColumn != null) usernameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        if (nameColumn != null) nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        if (roleColumn != null) roleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRole() != null ? cell.getValue().getRole().name() : ""));
        userTable.setItems(usuarios);
        userRefreshButton.setOnAction(e -> fetchAllUsers());
        userSearchField.textProperty().addListener((obs, oldVal, newVal) -> filterUsers(newVal));
        fetchAllUsers();
        
        carregarProdutosEstoque();

        if (productIdSearchField != null) {
            productIdSearchField.setOnAction(e -> handleProductIdSearch());
        }
        stockTable.setRowFactory(tv -> {
            TableRow<ProdutoDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ProdutoDTO produto = row.getItem();
                    abrirDetalheProduto(produto);
                }
            });
            return row;
        });
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adicionar-produto.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Adicionar Produto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarProdutosEstoque();
        } catch (Exception e) {
            showError("Erro ao abrir tela de adicionar produto", e.getMessage());
        }
    }

    @FXML
    private void handleAddUser() {
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

    private boolean validateProductFields() {
        if (nomeField.getText().isBlank()
                || precoField.getText().isBlank()
                || quantidadeEstoqueField.getText().isBlank()
                || estoqueMinimoField.getText().isBlank()
                || validadePicker.getValue() == null
                || categoriaComboBox.getValue() == null) {
            showError("Campos obrigatórios", "Preencha todos os campos do produto.");
            return false;
        }
        return true;
    }

    private boolean validateUserFields() {
        if (usernameField.getText().isBlank()
                || emailField.getText().isBlank()
                || senhaField.getText().isBlank()) {
            showError("Campos obrigatórios", "Preencha todos os campos do usuário.");
            return false;
        }
        return true;
    }

    private void clearProductFields() {
        nomeField.clear();
        precoField.clear();
        quantidadeEstoqueField.clear();
        estoqueMinimoField.clear();
        validadePicker.setValue(null);
        categoriaComboBox.setValue(null);
    }

    private void clearUserFields() {
        usernameField.clear();
        emailField.clear();
        senhaField.clear();
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

    private void showFeatureInDevelopment(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Funcionalidade em Desenvolvimento");
        alert.setContentText("A funcionalidade de " + title.toLowerCase() + " será implementada em breve.");
        alert.showAndWait();
    }

    private void carregarProdutosEstoque() {
        javafx.concurrent.Task<ProdutoDTO[]> task = new javafx.concurrent.Task<>() {
            @Override
            protected ProdutoDTO[] call() throws Exception {
                return restTemplate.getForObject(API_URL_PRODUTOS, ProdutoDTO[].class);
            }
        };
        task.setOnSucceeded(e -> {
            produtos.setAll(task.getValue());
            stockTable.setItems(produtos);
        });
        task.setOnFailed(e -> {
            stockTable.setItems(FXCollections.observableArrayList());
            showError("Erro ao carregar produtos", task.getException().getMessage());
        });
        new Thread(task).start();
    }

    private void showJsonDialog(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            TextArea textArea = new TextArea(json);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("JSON do Relatório");
            dialog.setScene(new Scene(textArea, 600, 400));
            dialog.showAndWait();
        } catch (Exception e) {
            showError("Erro ao exibir JSON", e.getMessage());
        }
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

    private void filterUsers(String query) {
        if (query == null || query.isBlank()) {
            userTable.setItems(usuarios);
            return;
        }
        ObservableList<Usuario> filtered = FXCollections.observableArrayList(
            usuarios.stream().filter(u -> u.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                u.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                (u.getRole() != null && u.getRole().name().toLowerCase().contains(query.toLowerCase()))
            ).toList()
        );
        userTable.setItems(filtered);
    }

    @FXML
    private void handleProductSearch() {
        String query = productSearchField.getText();
        categoria selectedCat = categoriaComboBox.getValue();
        ObservableList<ProdutoDTO> filtered = produtos.filtered(p ->
            (query == null || query.isBlank() || p.getNome().toLowerCase().contains(query.toLowerCase())) &&
            (selectedCat == null || p.getCategoria() == selectedCat)
        );
        stockTable.setItems(filtered);
    }

    @FXML
    private void handleOpenVendasReport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorio-vendas.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Relatório de Vendas");
            stage.setScene(new Scene(root, UIConfig.getPopupWidth(), UIConfig.getPopupHeight()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            showError("Erro ao abrir relatório de vendas", e.getMessage());
        }
    }

    @FXML
    private void handleOpenEstoqueBaixoReport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorio-estoque-baixo.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Relatório de Estoque Baixo");
            stage.setScene(new Scene(root, UIConfig.getPopupWidth(), UIConfig.getPopupHeight()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            showError("Erro ao abrir relatório de estoque baixo", e.getMessage());
        }
    }

    @FXML
    private void handleOpenValidadeReport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorio-validade.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Relatório de Produtos Próximos do Vencimento");
            stage.setScene(new Scene(root, UIConfig.getPopupWidth(), UIConfig.getPopupHeight()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            showError("Erro ao abrir relatório de validade", e.getMessage());
        }
    }

    @FXML
    private void handleOpenHorarioPicoReport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorio-horario-pico.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Relatório de Horários de Pico");
            stage.setScene(new Scene(root, UIConfig.getPopupWidth(), UIConfig.getPopupHeight()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            showError("Erro ao abrir relatório de horários de pico", e.getMessage());
        }
    }

    @FXML
    private void handleProductIdSearch() {
        String idStr = productIdSearchField.getText();
        if (idStr == null || idStr.isBlank()) {
            stockTable.setItems(produtos);
            return;
        }
        try {
            Long id = Long.parseLong(idStr.trim());
            javafx.concurrent.Task<ProdutoDTO> task = new javafx.concurrent.Task<>() {
                @Override
                protected ProdutoDTO call() throws Exception {
                    return restTemplate.getForObject(API_URL_PRODUTOS + "/" + id, ProdutoDTO.class);
                }
            };
            task.setOnSucceeded(e -> {
                ProdutoDTO result = task.getValue();
                if (result != null) {
                    stockTable.setItems(FXCollections.observableArrayList(result));
                } else {
                    stockTable.setItems(FXCollections.observableArrayList());
                }
            });
            task.setOnFailed(e -> {
                stockTable.setItems(FXCollections.observableArrayList());
                showError("Produto não encontrado", "ID informado não existe.");
            });
            new Thread(task).start();
        } catch (NumberFormatException e) {
            showError("ID inválido", "Digite um número válido para o ID.");
        }
    }

    private void abrirDetalheProduto(ProdutoDTO produto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/produto-detalhe.fxml"));
            Parent root = loader.load();
            ProdutoDetalheController controller = loader.getController();
            controller.setProduto(produto);
            controller.setAdminCredentials(adminUserId, adminPassword);
            Stage stage = new Stage();
            stage.setTitle("Detalhes do Produto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarProdutosEstoque();
        } catch (Exception e) {
            showError("Erro ao abrir detalhes do produto", e.getMessage());
        }
    }
}
