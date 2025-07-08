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

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

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

    private void validatePayment(Pedido pedido, BigDecimal amount) {
        if (amount.compareTo(pedido.getValorTotal()) < 0) {
            throw new RuntimeException("Valor insuficiente para pagamento");
        }
    }

    private String generatePixCode(Pedido pedido) {
        return "PIX" + System.currentTimeMillis();
    }

    public Pagamento processarPagamento(Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pagamento.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.PENDENTE);

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento processarPagamentoCartao(Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pagamento.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.APROVADO);

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento processarPagamentoPix(Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pagamento.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.Status.PENDENTE);
        pagamento.setCodigo_pix(gerarCodigoPix());

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento verificarStatus(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }

    public Pagamento cancelarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (pagamento.getMetodo() == Pagamento.MetodoPagamento.PIX) {
            pagamento.setStatus(Pagamento.Status.CANCELADO);
            return pagamentoRepository.save(pagamento);
        }

        throw new RuntimeException("Apenas pagamentos PIX podem ser cancelados");
    }

    private String gerarCodigoPix() {
        return "PIX" + System.currentTimeMillis();
    }

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
            pagamento.setStatus(Pagamento.Status.PENDENTE);
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
