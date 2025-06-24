package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.Produto;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


@Controller
public class ShoppingCartController {

    @FXML
    private TableView<Item_pedido> cartTable;

    @FXML
    private TableColumn<Item_pedido, String> productColumn;

    @FXML
    private TableColumn<Item_pedido, Integer> quantityColumn;

    @FXML
    private TableColumn<Item_pedido, BigDecimal> priceColumn;

    @FXML
    private TableColumn<Item_pedido, BigDecimal> totalColumn;

    @FXML
    private TableColumn<Item_pedido, Void> actionsColumn;

    @FXML
    private Label totalLabel;

    private final ObservableList<Item_pedido> cartItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        updateTotal();
    }

    private void setupTableColumns() {
        productColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProduto().getNome())
        );
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        totalColumn.setCellValueFactory(cellData -> {
            BigDecimal subtotal = cellData.getValue().getSubtotal();
            return new ReadOnlyObjectWrapper<>(subtotal != null ? subtotal : BigDecimal.ZERO);
        });

        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remover");

            {
                removeButton.setOnAction(event -> {
                    Item_pedido item = getTableView().getItems().get(getIndex());
                    handleRemoveItem(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });

        cartTable.setItems(cartItems);
    }

    public void addItem(Produto produto, int quantidade) {
        if (produto.getPreco() == null) {
            showAlert("Erro", "Produto \"" + produto.getNome() + "\" não possui preço definido.");
            return;
        }

        Item_pedido existingItem = cartItems.stream()
                .filter(item -> item.getProduto().equals(produto))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantidade(existingItem.getQuantidade() + quantidade);
            cartTable.refresh();
        } else {
            Item_pedido newItem = new Item_pedido();
            newItem.setProduto(produto);
            newItem.setQuantidade(quantidade);
            newItem.setPrecoUnitario(produto.getPreco());
            cartItems.add(newItem);
        }

        updateTotal();
    }

    private void handleRemoveItem(Item_pedido item) {
        cartItems.remove(item);
        updateTotal();
    }

    private void updateTotal() {
        BigDecimal total = cartItems.stream()
                .map(Item_pedido::getSubtotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalLabel.setText(String.format("R$ %.2f", total));
    }

    @FXML
    private void handleContinueShopping() {
        MainController.getInstance().loadView("product-selection");
    }

    @FXML
    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert("Carrinho vazio", "Adicione itens ao carrinho antes de finalizar o pedido.");
            return;
        }
        MainController.getInstance().loadView("payment");
    }

    public ObservableList<Item_pedido> getCartItems() {
        return cartItems;
    }

    private BigDecimal calculateTotal() {
        return cartItems.stream()
                .map(Item_pedido::getSubtotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotal() {
        return calculateTotal();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
