package com.cantinasa.cantinasa.exceptions;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException() {
        super("Pedido não encontrado");
    }
    public PedidoNotFoundException(String message) {
        super(message);
    }
} 