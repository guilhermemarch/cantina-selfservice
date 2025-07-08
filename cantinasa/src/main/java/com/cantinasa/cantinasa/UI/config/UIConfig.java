package com.cantinasa.cantinasa.UI.config;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.stage.Screen;

public class UIConfig {
    public static final Color PRIMARY_COLOR = Color.web("#2196F3");
    public static final Color SECONDARY_COLOR = Color.web("#4CAF50");
    public static final Color BACKGROUND_COLOR = Color.web("#f5f5f5");
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color BORDER_COLOR = Color.web("#e0e0e0");
    
    public static final Font TITLE_FONT = Font.font("System", 18);
    public static final Font HEADER_FONT = Font.font("System", 16);
    public static final Font NORMAL_FONT = Font.font("System", 14);
    public static final Font SMALL_FONT = Font.font("System", 12);
    
    public static final double CARD_PADDING = 10.0;
    public static final double GRID_SPACING = 10.0;
    public static final double BUTTON_PADDING = 8.0;
    
    public static final int PRODUCTS_PER_ROW = 4;
    public static final double MIN_CARD_WIDTH = 200.0;
    public static final double MIN_CARD_HEIGHT = 150.0;
    
    public static final String[] PRODUCT_CATEGORIES = {
        "Todos",
        "Bebidas",
        "Lanches",
        "Doces",
        "Salgados"
    };
    
    public static final String[] PAYMENT_METHODS = {
        "DINHEIRO",
      //  "CARTAO",
        "PIX"
    };
    
    public static final String[] ORDER_STATUS = {
        "CONCLUIDO",
        "CANCELADO",
        "PENDENTE"
    };

    private static final Properties properties = new Properties();
    static {
        try (InputStream input = UIConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getDefaultWindowWidth() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        if (screenWidth >= 1920) {
            return Integer.parseInt(properties.getProperty("ui.window.width.fullhd", "1024"));
        } else if (screenWidth >= 1336) {
            return Integer.parseInt(properties.getProperty("ui.window.width.hd", "900"));
        } else {
            return Integer.parseInt(properties.getProperty("ui.window.min.width", "400"));
        }
    }

    public static int getDefaultWindowHeight() {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        if (screenHeight >= 1080) {
            return Integer.parseInt(properties.getProperty("ui.window.height.fullhd", "768"));
        } else if (screenHeight >= 768) {
            return Integer.parseInt(properties.getProperty("ui.window.height.hd", "600"));
        } else {
            return Integer.parseInt(properties.getProperty("ui.window.min.height", "300"));
        }
    }

    public static int getMinWindowWidth() {
        return Integer.parseInt(properties.getProperty("ui.window.min.width", "400"));
    }

    public static int getMinWindowHeight() {
        return Integer.parseInt(properties.getProperty("ui.window.min.height", "300"));
    }

    public static int getPopupWidth() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        if (screenWidth >= 1920) {
            return Integer.parseInt(properties.getProperty("ui.popup.width.fullhd", "900"));
        } else if (screenWidth >= 1336) {
            return Integer.parseInt(properties.getProperty("ui.popup.width.hd", "700"));
        } else {
            return Integer.parseInt(properties.getProperty("ui.popup.min.width", "400"));
        }
    }

    public static int getPopupHeight() {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        if (screenHeight >= 1080) {
            return Integer.parseInt(properties.getProperty("ui.popup.height.fullhd", "700"));
        } else if (screenHeight >= 768) {
            return Integer.parseInt(properties.getProperty("ui.popup.height.hd", "500"));
        } else {
            return Integer.parseInt(properties.getProperty("ui.popup.min.height", "300"));
        }
    }
} 