package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.model.dto.PagamentoProcessarRequest;
import com.cantinasa.cantinasa.service.PagamentoService;
import com.cantinasa.cantinasa.exceptions.PagamentoExistenteException;
import com.cantinasa.cantinasa.exceptions.PedidoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/processar")
    public ResponseEntity<?> processarPagamento(@RequestBody PagamentoProcessarRequest request) {
        try {
            return ResponseEntity.ok(pagamentoService.processarPagamentoFromRequest(request));
        } catch (PagamentoExistenteException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (PedidoNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> verificarStatus(@PathVariable Long id) {
        try {
            Pagamento pagamento = pagamentoService.verificarStatus(id);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Pagamento> cancelarPagamento(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pagamentoService.cancelarPagamento(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
