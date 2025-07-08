package com.cantinasa.cantinasa.exceptions;

public class PagamentoExistenteException extends RuntimeException {
    public PagamentoExistenteException() {
        super("Já existe pagamento para este pedido");
    }
    public PagamentoExistenteException(String message) {
        super(message);
    }
} 