package com.cantinasa.cantinasa.UI.styles;

import com.cantinasa.cantinasa.UI.config.UIConfig;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class UIStyles {
    private static final double SHADOW_RADIUS = 10;
    private static final double SHADOW_SPREAD = 0.1;
    private static final Color SHADOW_COLOR = Color.rgb(0, 0, 0, 0.2);
    
    public static void applyMainContainerStyle(VBox container) {
        container.setBackground(new Background(new BackgroundFill(UIConfig.BACKGROUND_COLOR, null, null)));
        container.setPadding(new javafx.geometry.Insets(UIConfig.CARD_PADDING));
        container.setSpacing(UIConfig.GRID_SPACING);
        container.setStyle("-fx-background-color: linear-gradient(to bottom right, " + 
            toRGBCode(UIConfig.BACKGROUND_COLOR) + ", " + 
            toRGBCode(UIConfig.BACKGROUND_COLOR.darker()) + ");");
    }
    
    public static void applyProductCardStyle(VBox card) {
        card.setBackground(new Background(new BackgroundFill(Color.WHITE, 
            new CornerRadii(10), null)));
        card.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
            BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        card.setPadding(new javafx.geometry.Insets(UIConfig.CARD_PADDING));
        
        DropShadow shadow = new DropShadow(BlurType.GAUSSIAN, SHADOW_COLOR, 
            SHADOW_RADIUS, SHADOW_SPREAD, 0, 0);
        card.setEffect(shadow);
        
        card.setOnMouseEntered(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), card);
            ft.setFromValue(1.0);
            ft.setToValue(0.9);
            ft.play();
            card.setTranslateY(-2);
        });
        
        card.setOnMouseExited(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), card);
            ft.setFromValue(0.9);
            ft.setToValue(1.0);
            ft.play();
            card.setTranslateY(0);
        });
    }
    
    public static void applyButtonStyle(Button button) {
        button.setBackground(new Background(new BackgroundFill(UIConfig.PRIMARY_COLOR, 
            new CornerRadii(8), null)));
        button.setTextFill(Color.WHITE);
        button.setPadding(new javafx.geometry.Insets(UIConfig.BUTTON_PADDING));
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        button.setOnMouseEntered(e -> {
            button.setBackground(new Background(new BackgroundFill(
                UIConfig.PRIMARY_COLOR.brighter(), new CornerRadii(8), null)));
        });
        
        button.setOnMouseExited(e -> {
            button.setBackground(new Background(new BackgroundFill(
                UIConfig.PRIMARY_COLOR, new CornerRadii(8), null)));
        });
    }
    
    public static void applyTextFieldStyle(TextField textField) {
        textField.setBackground(new Background(new BackgroundFill(Color.WHITE, 
            new CornerRadii(8), null)));
        textField.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
            BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
        textField.setPadding(new javafx.geometry.Insets(10));
        textField.setStyle("-fx-font-size: 14px;");
        
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setBorder(new Border(new BorderStroke(UIConfig.PRIMARY_COLOR, 
                    BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2))));
            } else {
                textField.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
                    BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
            }
        });
    }
    
    public static void applyComboBoxStyle(ComboBox<?> comboBox) {
        comboBox.setBackground(new Background(new BackgroundFill(Color.WHITE, 
            new CornerRadii(8), null)));
        comboBox.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
            BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
        comboBox.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBox.setBorder(new Border(new BorderStroke(UIConfig.PRIMARY_COLOR, 
                    BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2))));
            } else {
                comboBox.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
                    BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
            }
        });
    }
    
    public static void applyTableViewStyle(TableView<?> tableView) {
        tableView.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        tableView.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
            BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
        tableView.setStyle("-fx-font-size: 14px;");
        
        tableView.lookup(".column-header").setStyle(
            "-fx-background-color: " + toRGBCode(UIConfig.PRIMARY_COLOR) + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
    }
    
    public static void applyTabPaneStyle(TabPane tabPane) {
        tabPane.setBackground(new Background(new BackgroundFill(UIConfig.BACKGROUND_COLOR, null, null)));
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent;");
    }
    
    public static void applyTabStyle(Tab tab) {
        tab.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold;"
        );
        
        tab.getContent().setOnMouseEntered(e -> {
            tab.setStyle(
                "-fx-background-color: " + toRGBCode(Color.rgb(
                    (int)(UIConfig.PRIMARY_COLOR.getRed() * 255),
                    (int)(UIConfig.PRIMARY_COLOR.getGreen() * 255),
                    (int)(UIConfig.PRIMARY_COLOR.getBlue() * 255),
                    0.1)) + "; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;"
            );
        });
        
        tab.getContent().setOnMouseExited(e -> {
            tab.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;"
            );
        });
    }
    
    public static void applyAlertStyle(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setBackground(new Background(new BackgroundFill(Color.WHITE, 
            new CornerRadii(10), null)));
        dialogPane.setBorder(new Border(new BorderStroke(UIConfig.BORDER_COLOR, 
            BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        
        dialogPane.lookup(".header-panel").setStyle(
            "-fx-background-color: " + toRGBCode(UIConfig.PRIMARY_COLOR) + "; " +
            "-fx-text-fill: white;"
        );
        
        for (ButtonType buttonType : alert.getButtonTypes()) {
            Node button = dialogPane.lookupButton(buttonType);
            button.setStyle(
                "-fx-background-color: " + toRGBCode(UIConfig.PRIMARY_COLOR) + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 16px; " +
                "-fx-background-radius: 8px;"
            );
        }
    }
    
    public static void applyLoadingStyle(Node node) {
        node.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.8); " +
            "-fx-background-radius: 10px;"
        );
        
        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.setCycleCount(FadeTransition.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }
    
    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
} 