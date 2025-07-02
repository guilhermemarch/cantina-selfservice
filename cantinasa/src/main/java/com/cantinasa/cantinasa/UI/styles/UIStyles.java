package com.cantinasa.cantinasa.UI.styles;

import com.cantinasa.cantinasa.UI.config.UIConfig;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public final class UIStyles {

    private static final double SHADOW_RADIUS = 10;
    private static final double SHADOW_SPREAD = 0.10;
    private static final Color  SHADOW_COLOR  = Color.rgb(0, 0, 0, 0.20);

    private static final double BORDER_RADIUS  = 10;
    private static final double BUTTON_RADIUS  = 8;
    private static final double ANIM_DURATION = 200;

    private UIStyles() {
    }

    public static void applyMainContainerStyle(VBox container) {
        container.setBackground(new Background(new BackgroundFill(UIConfig.BACKGROUND_COLOR, null, null)));
        container.setPadding(new Insets(UIConfig.CARD_PADDING));
        container.setSpacing(UIConfig.GRID_SPACING);

        container.setStyle("-fx-background-color: linear-gradient(to bottom right, " +
                toRGBCode(UIConfig.BACKGROUND_COLOR) + ", " +
                toRGBCode(UIConfig.BACKGROUND_COLOR.darker()) + ");");
    }

    public static void applyProductCardStyle(VBox card) {
        roundCorners(card, BORDER_RADIUS);
        card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(BORDER_RADIUS), null)));
        card.setBorder(createBorder(UIConfig.BORDER_COLOR, BORDER_RADIUS, 1));
        card.setPadding(new Insets(UIConfig.CARD_PADDING));
        addShadow(card);
        addHoverFade(card, 1.0, 0.92, -2);
    }

    public static void applyButtonStyle(Button button) {
        roundCorners(button, BUTTON_RADIUS);
        button.setBackground(new Background(new BackgroundFill(UIConfig.PRIMARY_COLOR, new CornerRadii(BUTTON_RADIUS), null)));
        button.setTextFill(Color.WHITE);
        button.setPadding(new Insets(UIConfig.BUTTON_PADDING));
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        button.setOnMouseEntered(e -> button.setBackground(new Background(new BackgroundFill(UIConfig.PRIMARY_COLOR.brighter(), new CornerRadii(BUTTON_RADIUS), null))));
        button.setOnMouseExited (e -> button.setBackground(new Background(new BackgroundFill(UIConfig.PRIMARY_COLOR,            new CornerRadii(BUTTON_RADIUS), null))));
    }

    public static void applyTextFieldStyle(TextField textField) {
        roundCorners(textField, BUTTON_RADIUS);
        textField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(BUTTON_RADIUS), null)));
        textField.setBorder(createBorder(UIConfig.BORDER_COLOR, BUTTON_RADIUS, 1));
        textField.setPadding(new Insets(10));
        textField.setStyle("-fx-font-size: 14px;");

        textField.focusedProperty().addListener((obs, oldVal, hasFocus) ->
                textField.setBorder(createBorder(hasFocus ? UIConfig.PRIMARY_COLOR : UIConfig.BORDER_COLOR, BUTTON_RADIUS, hasFocus ? 2 : 1)));
    }

    public static void applyComboBoxStyle(ComboBox<?> comboBox) {
        roundCorners(comboBox, BUTTON_RADIUS);
        comboBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(BUTTON_RADIUS), null)));
        comboBox.setBorder(createBorder(UIConfig.BORDER_COLOR, BUTTON_RADIUS, 1));
        comboBox.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        comboBox.focusedProperty().addListener((obs, oldVal, hasFocus) ->
                comboBox.setBorder(createBorder(hasFocus ? UIConfig.PRIMARY_COLOR : UIConfig.BORDER_COLOR, BUTTON_RADIUS, hasFocus ? 2 : 1)));
    }

    public static void applyTableViewStyle(TableView<?> tableView) {
        roundCorners(tableView, BORDER_RADIUS);
        tableView.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        tableView.setBorder(createBorder(UIConfig.BORDER_COLOR, BORDER_RADIUS, 1));
        tableView.setStyle("-fx-font-size: 14px;");

        tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> tableView.lookupAll(".column-header-background").forEach(header ->
                header.setStyle("-fx-background-color: " + toRGBCode(UIConfig.PRIMARY_COLOR) + ";")));
        tableView.lookupAll(".column-header, .label").forEach(label -> label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"));
    }

    public static void applyTabPaneStyle(TabPane tabPane) {
        tabPane.setBackground(new Background(new BackgroundFill(UIConfig.BACKGROUND_COLOR, null, null)));
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent;");
        tabPane.getTabs().forEach(UIStyles::applyTabStyle);
    }

    public static void applyTabStyle(Tab tab) {
        tab.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-font-weight: bold;");
        Node content = tab.getContent();
        if (content != null) {
            content.setOnMouseEntered(e -> tab.setStyle("-fx-background-color: " + toRGBA(UIConfig.PRIMARY_COLOR, 0.08) + "; -fx-font-size: 14px; -fx-font-weight: bold;"));
            content.setOnMouseExited (e -> tab.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-font-weight: bold;"));
        }
    }

    public static void applyAlertStyle(Alert alert) {
        DialogPane pane = alert.getDialogPane();
        roundCorners(pane, BORDER_RADIUS);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(BORDER_RADIUS), null)));
        pane.setBorder(createBorder(UIConfig.BORDER_COLOR, BORDER_RADIUS, 1));

        Node header = pane.lookup(".header-panel");
        if (header != null) header.setStyle("-fx-background-color: " + toRGBCode(UIConfig.PRIMARY_COLOR) + "; -fx-text-fill: white;");

        pane.getButtonTypes().forEach(type -> {
            Node btn = pane.lookupButton(type);
            btn.setStyle("-fx-background-color: " + toRGBCode(UIConfig.PRIMARY_COLOR) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: " + BUTTON_RADIUS + ";");
        });
    }

    public static void applyLoadingStyle(Node node) {
        node.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: " + BORDER_RADIUS + ";");
        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
        ft.setInterpolator(Interpolator.EASE_BOTH);
        ft.setFromValue(0.6);
        ft.setToValue(1.0);
        ft.setCycleCount(FadeTransition.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }

    private static void addShadow(Region region) {
        region.setEffect(new DropShadow(BlurType.GAUSSIAN, SHADOW_COLOR, SHADOW_RADIUS, SHADOW_SPREAD, 0, 0));
    }

    private static void addHoverFade(Region region, double from, double to, double translateY) {
        region.setOnMouseEntered(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(ANIM_DURATION), region);
            ft.setFromValue(from);
            ft.setToValue(to);
            ft.play();
            region.setTranslateY(translateY);
        });
        region.setOnMouseExited(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(ANIM_DURATION), region);
            ft.setFromValue(to);
            ft.setToValue(from);
            ft.play();
            region.setTranslateY(0);
        });
    }

    private static void roundCorners(Region region, double radius) {
        region.setBackground(new Background(new BackgroundFill(region.getBackground() == null ? Color.TRANSPARENT : region.getBackground().getFills().get(0).getFill(), new CornerRadii(radius), null)));
    }

    private static Border createBorder(Color color, double radius, double width) {
        return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(radius), new BorderWidths(width)));
    }

    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255));
    }

    private static String toRGBA(Color color, double alpha) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255),
                alpha);
    }
}
