package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;


    @PostMapping("/processar")
    public ResponseEntity<Pagamento> processarPagamento(@RequestBody Pagamento pagamento) {
        try {
            return ResponseEntity.ok(pagamentoService.processarPagamento(pagamento));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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
