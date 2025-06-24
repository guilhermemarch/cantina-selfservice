package com.cantinasa.cantinasa.UI.components;

import com.cantinasa.cantinasa.UI.config.UIConfig;
import com.cantinasa.cantinasa.model.Produto;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import com.cantinasa.cantinasa.UI.styles.UIStyles;

public class    UIComponents {
    
    public static VBox createProductCard(String name, double price, String imageUrl) {
        VBox card = new VBox(10);
        UIStyles.applyProductCardStyle(card);
        
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        
        Text nameText = new Text(name);
        nameText.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        Text priceText = new Text(String.format("R$ %.2f", price));
        priceText.setFont(Font.font("System", FontWeight.BOLD, 18));
        priceText.setFill(Color.GREEN);
        
        Button addToCartBtn = new Button("Adicionar ao Carrinho");
        UIStyles.applyButtonStyle(addToCartBtn);
        
        card.getChildren().addAll(imageView, nameText, priceText, addToCartBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        
        return card;
    }
    
    public static VBox createShoppingCartItem(String name, int quantity, double price) {
        VBox item = new VBox(5);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");
        
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        Text nameText = new Text(name);
        nameText.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Button removeBtn = new Button("×");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px;");
        
        header.getChildren().addAll(nameText, removeBtn);
        HBox.setHgrow(nameText, Priority.ALWAYS);
        
        HBox details = new HBox(20);
        details.setAlignment(Pos.CENTER_LEFT);
        
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, quantity);
        quantitySpinner.setEditable(true);
        UIStyles.applyTextFieldStyle(quantitySpinner.getEditor());
        
        Text priceText = new Text(String.format("R$ %.2f", price));
        priceText.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        details.getChildren().addAll(quantitySpinner, priceText);
        
        item.getChildren().addAll(header, details);
        
        return item;
    }
    
    public static VBox createLoadingIndicator() {
        VBox loadingBox = new VBox(10);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(20));
        
        ProgressIndicator progress = new ProgressIndicator();
        progress.setStyle("-fx-progress-color: " + toRGBCode(Color.BLUE) + ";");
        
        Text loadingText = new Text("Carregando...");
        loadingText.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        loadingBox.getChildren().addAll(progress, loadingText);
        UIStyles.applyLoadingStyle(loadingBox);
        
        return loadingBox;
    }
    
    public static Alert createConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        UIStyles.applyAlertStyle(alert);
        
        return alert;
    }
    
    public static Alert createErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        UIStyles.applyAlertStyle(alert);
        
        return alert;
    }
    
    public static void showNotification(StackPane root, String message) {
        VBox notification = new VBox(5);
        notification.setAlignment(Pos.CENTER);
        notification.setPadding(new Insets(15));
        notification.setStyle(
            "-fx-background-color: " + toRGBCode(Color.rgb(0, 0, 0, 0.8)) + "; " +
            "-fx-background-radius: 8px;"
        );
        
        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        notification.getChildren().add(text);
        
        notification.setTranslateY(-100);
        root.getChildren().add(notification);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), notification);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), notification);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        
        notification.setTranslateY(0);
        
        fadeIn.play();
        
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> {
                    fadeOut.play();
                    fadeOut.setOnFinished(e -> root.getChildren().remove(notification));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
} 