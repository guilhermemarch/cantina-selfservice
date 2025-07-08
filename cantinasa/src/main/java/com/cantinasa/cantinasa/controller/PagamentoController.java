package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.model.dto.PagamentoProcessarRequest;
import com.cantinasa.cantinasa.service.PagamentoService;
import com.cantinasa.cantinasa.exceptions.PagamentoExistenteException;
import com.cantinasa.cantinasa.exceptions.PedidoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelas operações relacionadas a pagamentos.
 */
@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    /**
     * Processa um novo pagamento com base nos dados enviados.
     *
     * @param request Objeto contendo as informações para processar o pagamento.
     * @return Resposta com o resultado do processamento ou mensagem de erro.
     */
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

    /**
     * Verifica o status de um pagamento pelo ID.
     *
     * @param id Identificador do pagamento.
     * @return Objeto Pagamento com status ou erro 404 se não encontrado.
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<?> verificarStatus(@PathVariable Long id) {
        try {
            Pagamento pagamento = pagamentoService.verificarStatus(id);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cancela um pagamento existente com base no ID informado.
     *
     * @param id Identificador do pagamento a ser cancelado.
     * @return Pagamento cancelado ou erro 404 se não encontrado.
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Pagamento> cancelarPagamento(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pagamentoService.cancelarPagamento(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
