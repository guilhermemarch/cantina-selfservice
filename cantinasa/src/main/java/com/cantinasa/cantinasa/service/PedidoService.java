package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.controller.ProdutoController;
import com.cantinasa.cantinasa.model.*;
import com.cantinasa.cantinasa.model.dto.PedidoDTO;
import com.cantinasa.cantinasa.model.dto.PedidoRequest;
import com.cantinasa.cantinasa.model.enums.status;
import com.cantinasa.cantinasa.model.enums.tipoPagamento;
import com.cantinasa.cantinasa.model.mapper.PedidoMapper;
import com.cantinasa.cantinasa.repository.PedidoRepository;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import com.cantinasa.cantinasa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo fluxo de criação e gerenciamento de pedidos.
 */
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
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Cria um novo pedido a partir de um DTO.
     */
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

    /**
     * Cria um pedido baseado nos dados recebidos de uma requisição.
     */
    @Transactional
    public Pedido createFromRequest(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setDataPedido(request.getDataHora());
        pedido.setStatus(Pedido.Status.valueOf(request.getStatus()));
        
        Usuario usuario;
        if (request.getUsuarioId() == 0) {
            usuario = getOrCreateUnknownUser();
        } else {
            usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        }
        pedido.setUsuario(usuario);
        
        List<Item_pedido> itens = request.getItens().stream()
                .map(itemRequest -> {
                    Item_pedido item = new Item_pedido();
                    Produto produto = produtoService.findById(itemRequest.getProdutoId());
                    item.setProduto(produto);
                    item.setQuantidade(itemRequest.getQuantidade());
                    item.setPrecoUnitario(produto.getPreco());
                    item.setPedido(pedido);
                    return item;
                })
                .collect(Collectors.toList());
        
        pedido.setItens(itens);
        
        BigDecimal valorTotal = itens.stream()
                .map(Item_pedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(valorTotal);
        
        if (request.getPagamento() != null) {
        }
        
        return pedidoRepository.save(pedido);
    }
    
    /**
     * Retorna um usuário padrão caso não seja informado um usuário válido.
     */
    private Usuario getOrCreateUnknownUser() {
        return usuarioRepository.findById(0L).orElseGet(() -> {
            Usuario unknownUser = new Usuario();
            unknownUser.setId(0L);
            unknownUser.setUsername("usuario_desconhecido");
            unknownUser.setEmail("desconhecido@cantina.com");
            unknownUser.setPassword("n/a");
            unknownUser.setRole(com.cantinasa.cantinasa.model.enums.role.USERS);
            return usuarioRepository.save(unknownUser);
        });
    }

    /** Recupera um pedido pelo id. */
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    /** Lista todos os pedidos existentes. */
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    /** Atualiza o status de um pedido. */
    @Transactional
    public Pedido update(Long id, Pedido pedido) {
        Pedido existingPedido = findById(id);
        existingPedido.setStatus(pedido.getStatus());
        return pedidoRepository.save(existingPedido);
    }

    /** Remove um pedido pelo id. */
    @Transactional
    public void delete(Long id) {
        Pedido pedido = findById(id);
        pedidoRepository.delete(pedido);
    }

    /**
     * Cancela um pedido entregue, devolvendo itens ao estoque.
     */
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

    /**
     * Atualiza somente o status do pedido.
     */
    public Pedido atualizarStatus(Long id, Pedido.Status status) {
        Pedido pedido = findById(id);
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    /** Busca pedidos dentro de um período. */
    public List<Pedido> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    /** Busca pedidos por status. */
    public List<Pedido> findByStatus(Pedido.Status status) {
        return pedidoRepository.findByStatus(status);
    }
}
