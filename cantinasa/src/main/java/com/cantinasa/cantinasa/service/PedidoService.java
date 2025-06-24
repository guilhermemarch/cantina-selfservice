package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.controller.ProdutoController;
import com.cantinasa.cantinasa.model.*;
import com.cantinasa.cantinasa.model.dto.PedidoDTO;
import com.cantinasa.cantinasa.model.enums.status;
import com.cantinasa.cantinasa.model.mapper.PedidoMapper;
import com.cantinasa.cantinasa.repository.PedidoRepository;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Transactional
    public Pedido create(PedidoDTO dto) {
        Pedido pedido = pedidoMapper.toEntity(dto);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(Pedido.Status.PENDENTE);

        for (Item_pedido item : pedido.getItens()) {
            item.setPedido(pedido);
        }

        BigDecimal valorTotal = pedido.getItens().stream()
                .map(Item_pedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(valorTotal);

        if (pedido.getPagamento() != null) {
            pedido.getPagamento().setPedido(pedido);
            pedido.getPagamento().setValor(valorTotal);
            if (pedido.getPagamento().getData_pagamento() == null) {
                pedido.getPagamento().setData_pagamento(LocalDateTime.now());
            }
            if (pedido.getPagamento().getStatus() == null) {
                pedido.getPagamento().setStatus(Pagamento.Status.PENDENTE);
            }
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido update(Long id, Pedido pedido) {
        Pedido existingPedido = findById(id);
        existingPedido.setStatus(pedido.getStatus());
        return pedidoRepository.save(existingPedido);
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = findById(id);
        pedidoRepository.delete(pedido);
    }

    @Transactional
    public void cancel(Long id) {
        Pedido pedido = findById(id);
        if (pedido.getStatus() != Pedido.Status.ENTREGUE) {
            throw new RuntimeException("Apenas pedidos entregues podem ser cancelados");
        }

        for (Item_pedido item : pedido.getItens()) {
            Produto produto = produtoService.findById(item.getProduto().getId());
            produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatus(Long id, Pedido.Status status) {
        Pedido pedido = findById(id);
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    public List<Pedido> findByStatus(Pedido.Status status) {
        return pedidoRepository.findByStatus(status);
    }
}
