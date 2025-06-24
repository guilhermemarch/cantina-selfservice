package com.cantinasa.cantinasa.UI.config;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
        "CARTAO",
        "PIX"
    };
    
    public static final String[] ORDER_STATUS = {
        "CONCLUIDO",
        "CANCELADO",
        "PENDENTE"
    };
} 