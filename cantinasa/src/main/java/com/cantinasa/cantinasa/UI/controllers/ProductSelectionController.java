package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.service.ProdutoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.cantinasa.cantinasa.UI.styles.UIStyles;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

import static com.cantinasa.cantinasa.model.enums.categoria.OUTROS;

@Controller
public class ProductSelectionController {

    @Autowired
    private ProdutoService produtoService;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private FlowPane productsFlow;

    @FXML
    public void initialize() {
        setupSearchField();
        setupCategoryFilter();
        loadProducts();
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts();
        });
    }

    private void setupCategoryFilter() {

        categoryFilter.getItems().addAll("Todos", "BEBIDA", "SALGADO", "DOCE", "REFEICAO", "SOBREMESA", "LANCHE", "OUTROS");
        categoryFilter.setValue("Todos");
        categoryFilter.setOnAction(event -> filterProducts());
    }

    private void loadProducts() {
        List<Produto> produtos = produtoService.findAll();
        displayProducts(produtos);
    }

    private void filterProducts() {
        String searchText = searchField.getText().toLowerCase();
        String selectedCategory = categoryFilter.getValue();

        List<Produto> filteredProducts = produtoService.findAll().stream()
                .filter(p -> p.getNome().toLowerCase().contains(searchText))
                .filter(p -> selectedCategory.equals("Todos") ||
                        p.getCategoria().name().equalsIgnoreCase(selectedCategory))
                .toList();

        displayProducts(filteredProducts);
    }


    private void displayProducts(List<Produto> produtos) {
        productsFlow.getChildren().clear();
        for (Produto produto : produtos) {
            VBox productCard = createProductCard(produto);
            productsFlow.getChildren().add(productCard);
        }
    }

    private VBox createProductCard(Produto produto) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(240);
        card.setPrefHeight(320);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        String imgUrl = produto.getImagemUrl();
        if (imgUrl != null && !imgUrl.isBlank()) {
            imageView.setImage(new Image(imgUrl, true));
        } else {
            imageView.setImage(new Image("file:/D:/github/apagar/cantinasa/cantinasa/src/main/resources/images/img_4.png", true));
        }

        Text nameText = new Text(produto.getNome());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        Text descText = new Text(produto.getDescricao());
        descText.setStyle("-fx-font-size: 14px; -fx-fill: #666666;");
        descText.setWrappingWidth(200);
        
        Text priceText = new Text(String.format("R$ %.2f", produto.getPreco()));
        priceText.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-fill: #4CAF50;");
        
        Text stockText = new Text("Em estoque: " + produto.getQuantidade());
        stockText.setStyle("-fx-font-size: 14px; -fx-fill: #666666;");
        
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER);
        
        Button minusBtn = new Button("-");
        minusBtn.getStyleClass().add("quantity-button");
        
        Label quantityLabel = new Label("1");
        quantityLabel.getStyleClass().add("quantity-label");
        
        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("quantity-button");
        
        minusBtn.setOnAction(e -> {
            int currentQty = Integer.parseInt(quantityLabel.getText());
            if (currentQty > 1) {
                quantityLabel.setText(String.valueOf(currentQty - 1));
            }
        });
        
        plusBtn.setOnAction(e -> {
            int currentQty = Integer.parseInt(quantityLabel.getText());
            if (currentQty < produto.getQuantidade()) {
                quantityLabel.setText(String.valueOf(currentQty + 1));
            } else {
                showAlert("Aviso", "Quantidade máxima disponível atingida!");
            }
        });
        
        quantityBox.getChildren().addAll(minusBtn, quantityLabel, plusBtn);
        
        Button addButton = new Button("Adicionar ao Carrinho");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityLabel.getText());
            handleAddToCart(produto, quantity);
        });
        
        card.getChildren().addAll(imageView, nameText, descText, priceText, stockText, quantityBox, addButton);
        return card;
    }

    @FXML
    private void handleAddToCart(Produto produto, int quantidade) {
        try {
            if (quantidade > produto.getQuantidade()) {
                showAlert("Erro", "Quantidade solicitada maior que o estoque disponível!");
                return;
            }

            ShoppingCartController cartController = MainController.getInstance().getShoppingCartController();
            if (cartController != null) {
                cartController.addItem(produto, quantidade);
                showAlert("Sucesso", quantidade + "x " + produto.getNome() + " adicionado ao carrinho!");
            } else {
                showAlert("Erro", "Carrinho não disponível. Por favor, tente novamente.");
            }
        } catch (Exception e) {
            showAlert("Erro", "Erro ao adicionar produto ao carrinho: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleViewCart() {
        MainController.getInstance().loadView("shopping-cart");
    }

    @FXML
    private void handleBack() {
        MainController.getInstance().loadView("welcome");
    }
} 