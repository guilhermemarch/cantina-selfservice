package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.model.*;
import com.cantinasa.cantinasa.model.dto.PagamentoProcessarRequest;
import com.cantinasa.cantinasa.repository.PagamentoRepository;
import com.cantinasa.cantinasa.repository.PedidoRepository;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import com.cantinasa.cantinasa.exceptions.PagamentoExistenteException;
import com.cantinasa.cantinasa.exceptions.PedidoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Serviço responsável pelo processamento dos pagamentos de pedidos.
 */
@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Processa pagamento em dinheiro atualizando estoque e calculando troco.
     */
    @Transactional
    public Pagamento processCashPayment(Pedido pedido, BigDecimal amountReceived) {
        validatePayment(pedido, amountReceived);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setMetodo(Pagamento.MetodoPagamento.DINHEIRO);
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.APROVADO);
        pagamento.setTroco(amountReceived.subtract(pedido.getValorTotal()).doubleValue());

        Pagamento savedPagamento = pagamentoRepository.save(pagamento);

        if (Pagamento.Status.APROVADO.equals(savedPagamento.getStatus())) {
            for (Item_pedido item : pedido.getItens()) {
                Produto produto = item.getProduto();
                int novaQuantidade = produto.getQuantidade() - item.getQuantidade();
                if (novaQuantidade < 0) novaQuantidade = 0;
                produto.setQuantidade(novaQuantidade);
                produtoRepository.save(produto);
            }
        }

        return savedPagamento;
    }

    /** Processa pagamento via cartão de crédito/débito. */
    @Transactional
    public Pagamento processCardPayment(Pedido pedido) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Erro ao processar pagamento com cartão");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setMetodo(Pagamento.MetodoPagamento.CARTAO);
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.APROVADO);

        return pagamentoRepository.save(pagamento);
    }

    /** Gera e processa pagamento via PIX. */
    @Transactional
    public Pagamento processPixPayment(Pedido pedido) {
        String pixCode = generatePixCode(pedido);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setMetodo(Pagamento.MetodoPagamento.PIX);
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.PENDENTE);
        pagamento.setCodigo_pix(pixCode);

        return pagamentoRepository.save(pagamento);
    }

    /** Valida se o valor recebido é suficiente. */
    private void validatePayment(Pedido pedido, BigDecimal amount) {
        if (amount.compareTo(pedido.getValorTotal()) < 0) {
            throw new RuntimeException("Valor insuficiente para pagamento");
        }
    }

    /** Cria um código PIX simples. */
    private String generatePixCode(Pedido pedido) {
        return "PIX" + System.currentTimeMillis();
    }

    /** Processa pagamento genérico. */
    public Pagamento processarPagamento(Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pagamento.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.PENDENTE);

        return pagamentoRepository.save(pagamento);
    }

    /** Processa pagamento no modo cartão. */
    public Pagamento processarPagamentoCartao(Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pagamento.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.APROVADO);

        return pagamentoRepository.save(pagamento);
    }

    /** Processa pagamento no modo PIX. */
    public Pagamento processarPagamentoPix(Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pagamento.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.PENDENTE);
        pagamento.setCodigo_pix(gerarCodigoPix());

        return pagamentoRepository.save(pagamento);
    }

    /** Obtém pagamento pelo id para verificar status. */
    public Pagamento verificarStatus(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }

    /**
     * Cancela um pagamento PIX existente.
     */
    public Pagamento cancelarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (pagamento.getMetodo() == Pagamento.MetodoPagamento.PIX) {
            pagamento.setStatus(Pagamento.Status.CANCELADO);
            return pagamentoRepository.save(pagamento);
        }

        throw new RuntimeException("Apenas pagamentos PIX podem ser cancelados");
    }

    /** Gera código PIX aleatório. */
    private String gerarCodigoPix() {
        return "PIX" + System.currentTimeMillis();
    }

    /**
     * Processa pagamento a partir de um objeto de requisição.
     */
    @Transactional
    public Pagamento processarPagamentoFromRequest(PagamentoProcessarRequest request) {
        Pedido pedido = pedidoRepository.findById(request.getPedido().getId())
                .orElseThrow(PedidoNotFoundException::new);

        if (pedido.getPagamento() != null) {
            throw new PagamentoExistenteException();
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(BigDecimal.valueOf(request.getValor()));
        pagamento.setMetodo(Pagamento.MetodoPagamento.valueOf(request.getMetodo()));
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setCodigo_pix(request.getCodigo_pix());
        pagamento.setTroco(request.getTroco());
        
        if (Pagamento.MetodoPagamento.PIX.equals(pagamento.getMetodo())) {
            pagamento.setStatus(Pagamento.Status.APROVADO);
        } else {
            pagamento.setStatus(Pagamento.Status.APROVADO);
        }

        Pagamento savedPagamento = pagamentoRepository.save(pagamento);

        if (Pagamento.Status.APROVADO.equals(savedPagamento.getStatus())) {
            for (Item_pedido item : pedido.getItens()) {
                Produto produto = item.getProduto();
                int novaQuantidade = produto.getQuantidade() - item.getQuantidade();
                if (novaQuantidade < 0) novaQuantidade = 0;
                produto.setQuantidade(novaQuantidade);
                produtoRepository.save(produto);
            }
        }

        return savedPagamento;
    }
}
